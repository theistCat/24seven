package uz.usoft.a24seven.network.models

class OrderResponse (
    val pagination: Pagination,
    val items: List<Order>
)