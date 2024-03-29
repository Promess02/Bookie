package mikolaj.michalczyk.readerapp.model

data class MUser(val id: String?,
val userId:String,
val displayName:String,
val avatarUrl: String,
val quote: String,
val profession: String,
val friendsList: List<String>){
    fun toMap():MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.userId,
             "display_name" to this.displayName,
            "quote" to this.quote,
            "profession" to this.profession,
            "avatarUrl" to this.avatarUrl,
            "friends_list" to this.friendsList
        )
    }
}
