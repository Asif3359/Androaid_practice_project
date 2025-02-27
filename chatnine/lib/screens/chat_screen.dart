import 'package:chatnine/screens/profile_screen.dart';
import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_contacts/contact.dart';
import '../models/message.dart';
import '../services/firebase_service.dart';
import 'UserProfile_screen.dart'; // Import the profile screen

class ChatScreen extends StatefulWidget {
  final Contact contact;
  final String contactEmail;

  const ChatScreen({Key? key, required this.contactEmail, required this.contact}) : super(key: key);

  @override
  _ChatScreenState createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  final TextEditingController _controller = TextEditingController();
  final FirebaseService _firebaseService = FirebaseService();
  final ScrollController _scrollController = ScrollController();

  String getUserEmail() => FirebaseAuth.instance.currentUser?.email ?? "unknown_user";

  String getChatId() {
    List<String> users = [getUserEmail(), widget.contactEmail];
    users.sort();
    return users.join('_');
  }

  void _sendMessage() {
    if (_controller.text.trim().isEmpty) return;
    String chatId = getChatId();

    Message message = Message(
      text: _controller.text.trim(),
      senderId: getUserEmail(),
      receiverId: widget.contactEmail,
      timestamp: Timestamp.now(),
    );

    _firebaseService.addMessage(chatId, message);
    _controller.clear();
    _scrollToBottom();
  }

  void _scrollToBottom() {
    Future.delayed(Duration(milliseconds: 300), () {
      _scrollController.animateTo(
        _scrollController.position.maxScrollExtent,
        duration: Duration(milliseconds: 300),
        curve: Curves.easeOut,
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    String chatId = getChatId();

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.blueAccent,
        elevation: 4, // Adds shadow effect
        title: InkWell(
          onTap: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => ProfileScreen(contact: widget.contact), // Navigate to Profile Screen
              ),
            );
          },
          borderRadius: BorderRadius.circular(30), // Adds tap effect
          child: Row(
            children: [
              // Profile Picture or First Letter Avatar
              CircleAvatar(
                radius: 22,
                backgroundColor: Colors.white, // White background for contrast
                child: widget.contact.photo != null
                    ? ClipOval(
                  child: Image.memory(
                    widget.contact.photo!,
                    fit: BoxFit.cover,
                    width: 44,
                    height: 44,
                  ),
                )
                    : Text(
                  widget.contact.displayName.isNotEmpty
                      ? widget.contact.displayName[0].toUpperCase()
                      : "?",
                  style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Colors.blueAccent),
                ),
              ),
              const SizedBox(width: 12), // Adds spacing

              // Name & Email
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    widget.contact.displayName.length > 15
                        ? '${widget.contact.displayName.substring(0, 8)}...' // Truncate name
                        : widget.contact.displayName,
                    style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.white),
                    overflow: TextOverflow.ellipsis, // Prevents overflow issues
                  ),
                  Text(
                    widget.contactEmail.length > 20
                        ? '${widget.contactEmail.substring(0, 16)}...' // Truncate email
                        : widget.contactEmail,
                    style: const TextStyle(fontSize: 14, color: Colors.white70),
                    overflow: TextOverflow.ellipsis, // Ensures smooth handling
                  ),
                ],
              ),

            ],
          ),
        ),
        centerTitle: false,
        // Voice Call & Video Call Icons
        actions: [
          // Voice Call Button
          IconButton(
            icon: const Icon(Icons.call, color: Colors.white),
            onPressed: () {
              // Implement voice call functionality
              print("Voice call to ${widget.contactEmail}");
            },
          ),

          // Video Call Button
          IconButton(
            icon: const Icon(Icons.videocam, color: Colors.white),
            onPressed: () {
              // Implement video call functionality
              print("Video call to ${widget.contactEmail}");
            },
          ),
        ],
      ),

      body: Column(
        children: <Widget>[
          Expanded(
            child: StreamBuilder<QuerySnapshot>(
              stream: _firebaseService.getMessagesStream(chatId),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return Center(child: CircularProgressIndicator());
                }
                if (!snapshot.hasData || snapshot.data!.docs.isEmpty) {
                  return Center(child: Text('No messages yet.', style: TextStyle(color: Colors.grey)));
                }

                final messages = snapshot.data!.docs
                    .map((doc) => Message.fromMap(doc.data() as Map<String, dynamic>))
                    .toList();

                return ListView.builder(
                  controller: _scrollController,
                  itemCount: messages.length,
                  itemBuilder: (context, index) {
                    final message = messages[index];
                    final isMe = message.senderId == getUserEmail();

                    return Align(
                      alignment: isMe ? Alignment.centerRight : Alignment.centerLeft,
                      child: Container(
                        margin: EdgeInsets.symmetric(vertical: 5, horizontal: 10),
                        padding: EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: isMe ? Colors.blue[300] : Colors.grey[300],
                          borderRadius: BorderRadius.circular(15),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              message.text,
                              style: TextStyle(fontSize: 16, color: isMe ? Colors.white : Colors.black),
                            ),
                            SizedBox(height: 4),
                            Text(
                              message.timestamp.toDate().toLocal().toString().split('.')[0],
                              style: TextStyle(fontSize: 12, color: Colors.black54),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                );
              },
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: <Widget>[
                Expanded(
                  child: TextField(
                    controller: _controller,
                    decoration: InputDecoration(
                      hintText: 'Type a message...',
                      filled: true,
                      fillColor: Colors.grey[200],
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(20),
                        borderSide: BorderSide.none,
                      ),
                    ),
                  ),
                ),
                SizedBox(width: 10),
                CircleAvatar(
                  backgroundColor: Colors.blue,
                  child: IconButton(
                    icon: Icon(Icons.send, color: Colors.white),
                    onPressed: _sendMessage,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
