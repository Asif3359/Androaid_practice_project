import 'package:flutter/material.dart';
import 'package:flutter_contacts/flutter_contacts.dart';
import 'package:url_launcher/url_launcher.dart';

class ProfileScreen extends StatelessWidget {
  final Contact contact;

  const ProfileScreen({super.key, required this.contact});

  void _makeCall(String phoneNumber) async {
    final Uri url = Uri.parse("tel:$phoneNumber");
    if (await canLaunchUrl(url)) {
      await launchUrl(url);
    }
  }

  void _sendMessage(String phoneNumber) async {
    final Uri url = Uri.parse("sms:$phoneNumber");
    if (await canLaunchUrl(url)) {
      await launchUrl(url);
    }
  }

  void _makeVideoCall(String phoneNumber) async {
    final Uri url = Uri.parse("facetime:$phoneNumber");
    if (await canLaunchUrl(url)) {
      await launchUrl(url);
    }
  }

  void _sendEmail(String email) async {
    final Uri url = Uri.parse("mailto:$email");
    if (await canLaunchUrl(url)) {
      await launchUrl(url);
    }
  }

  @override
  Widget build(BuildContext context) {
    String phoneNumber = contact.phones.isNotEmpty ? contact.phones.first.number : "No phone available";
    String email = contact.emails.isNotEmpty ? contact.emails.first.address : "No email available";

    return Scaffold(
      appBar: AppBar(
        title: Text(contact.displayName),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            // Profile Picture
            CircleAvatar(
              radius: 60,
              backgroundColor: Colors.grey.shade300,
              child: Text(
                contact.displayName.isNotEmpty ? contact.displayName[0].toUpperCase() : '?',
                style: const TextStyle(fontSize: 40, fontWeight: FontWeight.bold, color: Colors.black54),
              ),
            ),
            const SizedBox(height: 15),
            // Contact Name
            Text(
              contact.displayName,
              style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 5),
            // Phone Number
            Card(
              elevation: 2,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
              child: ListTile(
                leading: const Icon(Icons.phone, color: Colors.blueAccent),
                title: Text(phoneNumber, style: const TextStyle(fontSize: 16)),
              ),
            ),
            const SizedBox(height: 8),
            // Email
            Card(
              elevation: 2,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
              child: ListTile(
                leading: const Icon(Icons.email, color: Colors.redAccent),
                title: Text(email, style: const TextStyle(fontSize: 16)),
                onTap: email != "No email available" ? () => _sendEmail(email) : null,
              ),
            ),
            const SizedBox(height: 15),
            // Action Buttons (Call, Video, Message)
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                // Voice Call
                IconButton(
                  icon: const Icon(Icons.call, color: Colors.green, size: 30),
                  onPressed: phoneNumber != "No phone available" ? () => _makeCall(phoneNumber) : null,
                ),
                // Video Call
                IconButton(
                  icon: const Icon(Icons.video_call, color: Colors.blue, size: 30),
                  onPressed: phoneNumber != "No phone available" ? () => _makeVideoCall(phoneNumber) : null,
                ),
                // Message
                IconButton(
                  icon: const Icon(Icons.message, color: Colors.orange, size: 30),
                  onPressed: phoneNumber != "No phone available" ? () => _sendMessage(phoneNumber) : null,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
