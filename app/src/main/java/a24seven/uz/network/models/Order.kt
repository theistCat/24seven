package a24seven.uz.network.models

data class Order(
    val id: Int = 2,
    val products_count: Int = 1,
    val price_products: Long = 12000,
    val price_delivery: Int = 6,
    val status: String = "processing",
    val payment_type: String = "cash",
    val payment_status: String = "cash",
    val created_at: String = "08:45, 13.04.2021",
    val address: Address?,
    val products:List<OrderItem>?,
)