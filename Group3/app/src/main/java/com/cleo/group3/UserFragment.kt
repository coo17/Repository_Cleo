package com.cleo.group3

//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import androidx.databinding.DataBindingUtil.setContentView
//import com.cleo.group3.databinding.ActivityMainBinding
//import com.cleo.group3.databinding.FragmentUserBinding
//import com.google.firebase.firestore.FirebaseFirestore

//class UserFragment : Fragment() {
//
//    lateinit var firebase: FirebaseFirestore
//    lateinit var binding: FragmentUserBinding
//
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        // Inflate the layout for this fragment
////        binding = FragmentUserBinding.inflate(LayoutInflater.from())
////        return binding.root
////    }
//
//    companion object {
//        const val TAG = "user"
//    }
//
////
////    private var binding: ActivityMainBinding? = null
////    val firestoreDataBase = FirebaseFirestore.getInstance()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = FragmentUserBinding.inflate(LayoutInflater.from(this))
//        setContentView(binding.root)
//
//        firebase = FirebaseFirestore.getInstance()
//
//        firebase.collection("Users").addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && !snapshot.isEmpty) {
//                for (document in snapshot) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
////                Log.d(TAG, "Current data: ${snapshot }}")
//            } else {
//                Log.d(TAG, "Current data: null")
//            }
//        }
//
//        binding.signUpBtn.setOnClickListener {
//
//            getUserSignup(
//                binding.userName.text.toString(),
//                binding.userEmail.text.toString(),
//            )
//
//            addUserSignUp()
//        }
//    }
//
//    fun getUserSignup(userName: String, userEmail: String) {
//
//        val user: MutableMap<String, Any> = HashMap()
//        user["title"] = userName
//        user["content"] = userEmail
//        //Get Data
//        Log.d(TAG, "publishBtn")
//
//        firebase.collection("Users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "add success")
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//
//            }
//            .addOnFailureListener { e ->
//                Log.d(TAG, "add fail")
//                Log.w(TAG, "Error adding document", e)
//            }
//
//    }
//
//    fun addUserSignUp() {
//
//        //Read Data
//        firebase.collection("Users")
//            .get()
//            .addOnSuccessListener { result ->
//                Log.d(TAG, "get success")
//
//                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "get fail")
//                Log.w(TAG, "Error getting documents.", exception)
//            }
//
//    }
//}
