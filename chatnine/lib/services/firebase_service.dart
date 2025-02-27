import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/message.dart';

class FirebaseService {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  Future<void> addMessage(String chatId, Message message) async {
    await _firestore.collection('chats').doc(chatId).collection('messages').add(message.toMap());
    await _firestore.collection('chats').doc(chatId).set({
      'lastUpdated': Timestamp.now(),
    }, SetOptions(merge: true));
  }

  Stream<QuerySnapshot> getMessagesStream(String chatId) {
    return _firestore
        .collection('chats')
        .doc(chatId)
        .collection('messages')
        .orderBy('timestamp', descending: false) // Oldest first
        .snapshots();
  }
}
