package com.cleo.group3

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.cleo.group3.databinding.ActivityMainBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "cleooo"
    }

    lateinit var firebase: FirebaseFirestore
    lateinit var binding: ActivityMainBinding
    var targetId: String = ""
    var friendRequest: String = ""
//    var friendRequestEmailList: MutableList<MutableMap<String, Any>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        firebase = FirebaseFirestore.getInstance()

        firebase.collection("Data").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }

            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        binding.publishBtn.setOnClickListener {
            //get value inside the binding
            val tag = when (binding.chipGroup.checkedChipId) {
                R.id.tag1 -> "Beauty"
                R.id.tag2 -> "Gossiping"
                R.id.tag3 -> "SchoolLife"
                else -> {
                    ""
                }
            }
            addArticle(
                binding.title.text.toString(),
                binding.content.text.toString(),
                tag
            )
            readArticle()
        }


        val mID = "eHI8pklhdlwHDmmOZaV4"
        var mEmail = ""
        var mName = ""


        //設自己為USER
        firebase.collection("Users").document(mID).get().addOnSuccessListener {
            binding.loginUser.text = it.get("name").toString()
            mEmail = it.get("email").toString()
            mName = it.get("name").toString()
        }

        //誰寄交友邀請給我
        firebase.collection("Users").document(mID).collection("pending")
            .addSnapshotListener { request, e ->

                if (request == null || request.isEmpty) {

                    Log.d(TAG, "request=${request}")
                    return@addSnapshotListener
                }

                binding.whoSendRequest.text = request.first()?.get("email").toString()
                for (i in request) {
                    Log.d(TAG, "${i.data}")
                    friendRequest = request.first().get("id").toString()


//                    val friendInfo: MutableMap<String, Any> = HashMap()
//                    friendInfo["email"] = friendEmail
//                    friendInfo["id"] = friendId
//                    friendInfo["name"] = friendName
//                    val email = ???
//                    friendRequestEmailList.add(email)

//                    if (binding.whoSendRequest.text != null) {
//                        firebase.collection("Users")
//                            .whereEqualTo("email", binding.whoSendRequest.text.toString()).get()
//                            .addOnSuccessListener {
////             for (item in it){
////                 item.data
////                 Log.d(TAG, "$item.data")
//////             }
//                                if (request == null || request.isEmpty) {
//
//                                    Log.d(TAG, "request=${request}")
//                                    return@addSnapshotListener
//                                }
//                                friendRequest = it.first().id ?: ""
//                    Log.d(TAG, "${it.size()}")
                }
            }


        //寄出邀請
        binding.sendBtn.setOnClickListener {
            val myInfo: MutableMap<String, Any> = HashMap()
            myInfo["email"] = mEmail
            myInfo["id"] = mID
            myInfo["name"] = mName

            val targetEmail2 = binding.searchEmailText.text.toString()

            firebase.collection("Users")
                .whereEqualTo("email", targetEmail2).get()
                .addOnSuccessListener {
//                    if(targetId.isEmpty()){
//                        Toast.makeText(this, "wrong email", Toast.LENGTH_LONG).show();
//                    }else{
//
//                        return@addOnSuccessListener
//                    }

                    for (i in it) {
                        Log.d(TAG, i.id)
                        targetId = i.id
                    }

//                    Log.d(TAG, "${it.first().id}")

                    firebase.collection("Users").document(targetId).collection("pending").document(mID)
                        .set(myInfo).addOnSuccessListener {
                            Log.d(TAG, "add myInfo success")

                        }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "send fail")
                            Log.w(TAG, "Error adding document", e)
                        }

                }
        }



        binding.cfmBtn.setOnClickListener {

            //加對方到自己friends
            firebase.collection("Users").document(mID).collection("pending").document(friendRequest)
                .get().addOnSuccessListener {

                    val friendEmail = it.data?.get("email").toString()
                    val friendId = friendRequest
                    val friendName = it.data?.get("name").toString()
                    Log.d(TAG,"$friendName")


                    val friendInfo: MutableMap<String, Any> = HashMap()
                    friendInfo["email"] = friendEmail
                    friendInfo["id"] = friendId
                    friendInfo["name"] = friendName
                    firebase.collection("Users")
                        .document(mID)
                        .collection("friends")
                        .document(friendId)
                        .set(friendInfo)
                        .addOnSuccessListener {

                            Log.d(TAG, "add success")

                        }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "add fail")
                            Log.w(TAG, "Error adding document", e)
                        }
                }

            //加自己到對方friendlist
            val myInfo: MutableMap<String, Any> = HashMap()
            myInfo["email"] = mEmail
            myInfo["id"] = mID
            myInfo["name"] = mName

            firebase.collection("Users")
                .document(friendRequest)
                .collection("friends")
                .document(mID)
                .set(myInfo)
                .addOnSuccessListener {

                    Log.d(TAG, "add success")

                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "add fail")
                    Log.w(TAG, "Error adding document", e)
                }


//刪除pending
            firebase.collection("Users").document(mID).collection("pending").document(friendRequest)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

        }
    }

    fun addArticle(title: String, content: String, tag: String) {

        val user: MutableMap<String, Any> = HashMap()
        user["title"] = title
        user["content"] = content
        user["tag"] = tag
        user["author_id"] = "Cleo"
        user["created_time"] = Timestamp.now()

        //Get Data
        Log.d(TAG, "publishBtn")

        firebase.collection("Data")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "add success")
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

            }
            .addOnFailureListener { e ->
                Log.d(TAG, "add fail")
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun readArticle() {
        //Read Data
        firebase.collection("Data")
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "get success")

                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get fail")
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


//    fun searchEmail(email: String) {
//        firebase.collection("Users").whereEqualTo("email", email).get().addOnSuccessListener {
////             for (item in it){
////                 item.data
////                 Log.d(TAG, "$item.data")
////             }
//            targetId = it.first().id
//            Log.d(TAG, "${it.first().id}")
//        }
//    }

}