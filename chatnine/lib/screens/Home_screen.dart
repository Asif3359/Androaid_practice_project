import 'package:chatnine/screens/chat_screen.dart';
import 'package:chatnine/screens/contact_edit_screen.dart';
import 'package:flutter/material.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:flutter_facebook_auth/flutter_facebook_auth.dart';
import 'package:flutter_contacts/flutter_contacts.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  String selectedPage = 'Messages';
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final GoogleSignIn _googleSignIn = GoogleSignIn();
  User? _user;
  String? _loggedInWith;
  List<Contact> _contacts = [];

  @override
  void initState() {
    super.initState();
    _user = _auth.currentUser;
    _listenAuthChanges();
    _loadContacts();
  }

  void _listenAuthChanges() {
    _auth.authStateChanges().listen((User? user) {
      setState(() {
        _user = user;
      });
    });
  }

  Future<void> _loadContacts() async {
    if (await FlutterContacts.requestPermission()) {
      List<Contact> contacts = await FlutterContacts.getContacts(withProperties: true);
      setState(() {
        _contacts = contacts;
      });
    }
  }

  Future<void> _deleteContact(Contact contact) async {
    try {
      await contact.delete();
      setState(() {
        _contacts.remove(contact);
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('${contact.displayName} deleted successfully')),
      );
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Failed to delete ${contact.displayName}')),
      );
    }
  }

  void _confirmDeleteContact(Contact contact) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Delete Contact', style: TextStyle(fontWeight: FontWeight.bold)),
          content: Text('Are you sure you want to delete ${contact.displayName}?'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pop(context);
                _deleteContact(contact);
              },
              child: const Text('Delete', style: TextStyle(color: Colors.red)),
            ),
          ],
        );
      },
    );
  }

  void _logout() async {
    try {
      if (_loggedInWith == 'google') {
        await _googleSignIn.signOut();
      } else if (_loggedInWith == 'facebook') {
        await FacebookAuth.instance.logOut();
      }
      await _auth.signOut();
      setState(() {
        _loggedInWith = null;
      });
      if (mounted) {
        Navigator.pushReplacementNamed(context, '/');
      }
    } catch (e) {
      print("Logout failed: $e");
    }
  }

  void _showLogoutDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Logout', style: TextStyle(fontWeight: FontWeight.bold)),
          content: const Text('Are you sure you want to log out?'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pop(context);
                _logout();
              },
              child: const Text('Logout'),
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: RichText(
          text: const TextSpan(
            children: [
              TextSpan(
                text: "Chat",
                style: TextStyle(
                  fontSize: 28,
                  fontWeight: FontWeight.w600,
                  color: Color(0xFF03A9F4),
                ),
              ),
              TextSpan(
                text: "9",
                style: TextStyle(
                  fontSize: 28,
                  fontWeight: FontWeight.w600,
                  color: Color(0xFF11539E),
                ),
              ),
            ],
          ),
        ),
        elevation: 5.0,
        backgroundColor: Colors.white,
      ),
      drawer: _buildDrawer(),
      body: _buildPage(),
    );
  }

  Widget _buildDrawer() {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          UserAccountsDrawerHeader(
            decoration: const BoxDecoration(color: Color(0xFF2196F3)),
            accountName: Text(
              _user?.displayName ?? 'Guest',
              style: const TextStyle(color: Colors.white, fontSize: 20, fontWeight: FontWeight.bold),
            ),
            accountEmail: Text(
              _user?.email ?? '',
              style: const TextStyle(color: Colors.white70),
            ),
            currentAccountPicture: CircleAvatar(
              backgroundImage: _user?.photoURL != null
                  ? NetworkImage(_user!.photoURL!)
                  : const AssetImage('assets/images/default_avatar.png')
              as ImageProvider,
            ),
          ),
          ListTile(
            leading: const Icon(Icons.perm_contact_cal, color: Colors.blue),
            title: const Text('Contact', style: TextStyle(fontWeight: FontWeight.w500)),
            onTap: () {
              setState(() => selectedPage = 'Contact');
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: const Icon(Icons.message, color: Colors.blue),
            title: const Text('Messages', style: TextStyle(fontWeight: FontWeight.w500)),
            onTap: () {
              setState(() => selectedPage = 'Messages');
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: const Icon(Icons.account_circle, color: Colors.blue),
            title: const Text('Profile', style: TextStyle(fontWeight: FontWeight.w500)),
            onTap: () {
              setState(() => selectedPage = 'Profile');
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: const Icon(Icons.settings, color: Colors.blue),
            title: const Text('Settings', style: TextStyle(fontWeight: FontWeight.w500)),
            onTap: () {
              setState(() => selectedPage = 'Settings');
              Navigator.pop(context);
            },
          ),
          const Divider(),
          ListTile(
            leading: const Icon(Icons.logout, color: Colors.red),
            title: const Text('Logout', style: TextStyle(fontWeight: FontWeight.w500)),
            onTap: () {
              Navigator.pop(context);
              _showLogoutDialog();
            },
          ),
        ],
      ),
    );
  }

  Widget _buildPage() {
    switch (selectedPage) {
      case 'Contact':
        return _buildContactPage();
      case 'Messages':
        return const Center(child: Text('Message Page'));
      case 'Profile':
        return const Center(child: Text('Profile Page'));
      case 'Settings':
        return const Center(child: Text('Settings Page'));
      default:
        return const Center(child: Text('Home Page'));
    }
  }

  Widget _buildContactPage() {
    List<Contact> contactsWithEmail = _contacts
        .where((contact) => contact.emails.isNotEmpty)
        .toList();

    return contactsWithEmail.isEmpty
        ? const Center(child: Text("No contacts with email found"))
        : ListView.builder(
      itemCount: contactsWithEmail.length,
      itemBuilder: (context, index) {
        final contact = contactsWithEmail[index];
        return ListTile(
          leading: contact.photo == null
              ? const CircleAvatar(child: Icon(Icons.person, color: Colors.white))
              : CircleAvatar(backgroundImage: MemoryImage(contact.photo!)),
          title: Text(contact.displayName, style: const TextStyle(fontWeight: FontWeight.w500)),
          subtitle: Text(contact.emails.isNotEmpty ? contact.emails[0].address : 'No email', style: const TextStyle(color: Colors.grey)),
          trailing: PopupMenuButton<String>(
            icon: const Icon(Icons.more_vert, color: Colors.blue), // 3-dot menu
            onSelected: (value) {
              switch (value) {
                case 'edit':
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => ContactEditScreen(contact: contact),
                    ),
                  ).then((updatedContact) {
                    if (updatedContact != null) {
                      setState(() {
                        // Update the contact list with the modified contact
                        int index = _contacts.indexOf(contact);
                        if (index != -1) {
                          _contacts[index] = updatedContact;
                        }
                      });
                    }
                  });
                  break;
                case 'delete':
                  _confirmDeleteContact(contact);
                  break;
                case 'call':
                // Handle call action
                  break;
              }
            },
            itemBuilder: (BuildContext context) {
              return [
                const PopupMenuItem<String>(
                  value: 'edit',
                  child: Text('Edit'),
                ),
                const PopupMenuItem<String>(
                  value: 'delete',
                  child: Text('Delete'),
                ),
                const PopupMenuItem<String>(
                  value: 'call',
                  child: Text('Call'),
                ),
              ];
            },
          ),
            onTap: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => ChatScreen(contact: contact),
              ),
            );
          },
        );
      },
    );
  }
}
