package ir.prothch21.management.models

data class All(
    val id: String,
    val pay: Pay,
    val receive: String,
    val reminded: Reminded,
    val strDate: String,
    val timeless: Timeless,
    val title: String
)