package dev.alamodi.firebaseapp

data class News(
    val id: String = "",
    val title: String,
    val date: String,
    val details: String
) {

    companion object {
        fun newsFromJson(id: String, newsMap: MutableMap<String, Any>): News {
//            val date = (newsMap["date"] as Timestamp).toDate().toLocaleString()
            return News(
                id = id,
                title = newsMap["title"] as String,
                details = newsMap["details"] as String,
                date = newsMap["date"] as  String
            )
        }
    }
}