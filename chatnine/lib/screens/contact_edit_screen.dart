import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:flutter_contacts/flutter_contacts.dart';

class ContactEditScreen extends StatefulWidget {
  final Contact contact;
  const ContactEditScreen({super.key, required this.contact});

  @override
  _ContactEditScreenState createState() => _ContactEditScreenState();
}

class _ContactEditScreenState extends State<ContactEditScreen> {
  final _nameController = TextEditingController();
  final _phoneController = TextEditingController();
  XFile? _imageFile;
  final ImagePicker _picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    _nameController.text = widget.contact.displayName;
    if (widget.contact.phones.isNotEmpty) {
      _phoneController.text = widget.contact.phones[0].number;
    }
  }

  // Function to pick an image from the gallery
  Future<void> _pickImage() async {
    final XFile? pickedFile = await _picker.pickImage(source: ImageSource.gallery);
    if (pickedFile != null) {
      setState(() {
        _imageFile = pickedFile;
      });
    }
  }

  // Function to save the edited contact
  Future<void> _saveContact() async {
    setState(() {
      widget.contact.displayName = _nameController.text;
      if (_phoneController.text.isNotEmpty) {
        widget.contact.phones[0] = Phone(_phoneController.text);
      }
    });

    // Save the photo asynchronously if an image is selected
    if (_imageFile != null) {
      final bytes = await _imageFile!.readAsBytes();
      setState(() {
        widget.contact.photo = bytes;
      });
    }

    Navigator.pop(context, widget.contact);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Edit Contact'),
        actions: [
          IconButton(
            icon: const Icon(Icons.save),
            onPressed: _saveContact,
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            GestureDetector(
              onTap: _pickImage,
              child: CircleAvatar(
                radius: 50,
                backgroundImage: _imageFile == null
                    ? (widget.contact.photo != null
                    ? MemoryImage(widget.contact.photo!)
                    : const AssetImage('assets/images/default_avatar.png'))
                    : FileImage(_imageFile!.path as File),
                child: _imageFile == null
                    ? const Icon(Icons.camera_alt, color: Colors.white)
                    : null,
              ),
            ),
            const SizedBox(height: 16),
            TextField(
              controller: _nameController,
              decoration: const InputDecoration(labelText: 'Name'),
            ),
            const SizedBox(height: 8),
            TextField(
              controller: _phoneController,
              decoration: const InputDecoration(labelText: 'Phone Number'),
              keyboardType: TextInputType.phone,
            ),
          ],
        ),
      ),
    );
  }
}
