package uz.usoft.a24seven.network.di

import java.io.Serializable

class MockData (
)
{
    companion object {
        fun getProductList() : List<ProductObject>
        {
            var list= ArrayList<ProductObject>()
            list.add(ProductObject("Fanta","7 490 сум за кг ","4780069000178"))
            list.add(ProductObject("","20 490 сум за кг "))
            list.add(ProductObject("","7 490 сум за кг "))
            list.add(ProductObject(""))
            list.add(ProductObject(""))
            return list
        }

        fun getPopularProductList() : List<ProductObject>
        {
            var list= ArrayList<ProductObject>()
            list.add(ProductObject("","7 490 сум за кг "))
            list.add(ProductObject("","7 490 сум за кг "))
            list.add(ProductObject("","7 490 сум за кг "))
            list.add(ProductObject(""))
            list.add(ProductObject(""))
            return list
        }

        fun getCategoriesList(): List<ProductObject>
        {
            var list= ArrayList<ProductObject>()
            list.add(ProductObject("Еда","200 sum"))
            list.add(ProductObject("Одежда","300 sum"))
            list.add(ProductObject("Телефоны и гаджеты","500 sum"))
            list.add(ProductObject("Спорт товары"))
            list.add(ProductObject("Техника для кухни"))
            return list
        }

        fun getSubCategoriesList(): List<ProductObject>
        {
            var list= ArrayList<ProductObject>()
            list.add(ProductObject("Овощи","200 sum"))
            list.add(ProductObject("Фрукты","300 sum"))
            list.add(ProductObject("Сухофрукты / орехи","500 sum"))
            list.add(ProductObject("Вода и напитки"))
            list.add(ProductObject("Торты и сладости"))
            list.add(ProductObject("Хлеб и хлебо-булочные изделия"))
            list.add(ProductObject("Яйца и молочные продукты"))
            list.add(ProductObject("Мясо и мясные продукты"))
            return list
        }


        fun getFeedbackList(): List<ProductObject>
        {
            var list= ArrayList<ProductObject>()
            list.add(ProductObject("Овощи","200"))
            list.add(ProductObject("Фрукты","300"))
            list.add(ProductObject("Сухофрукты / орехи","500"))
            list.add(ProductObject("Вода и напитки"))
            list.add(ProductObject("Торты и сладости"))
            list.add(ProductObject("Хлеб и хлебо-булочные изделия"))
            list.add(ProductObject("Яйца и молочные продукты"))
            list.add(ProductObject("Мясо и мясные продукты"))
            return list
        }

        fun getPaymentMethodList(): List<PaymentMethodObject>
        {
            var list= ArrayList<PaymentMethodObject>()
            list.add(PaymentMethodObject("PayMe",true))
            list.add(PaymentMethodObject("Click"))
            list.add(PaymentMethodObject("Наличные"))
            return list
        }
    }

    data class ProductObject(
        val name:String="",
        val price:String="0 sum",
        val code:String=""
    ):Serializable

    data class FeedbackObject(
        val name:String=""
    )

    data class PaymentMethodObject(
        val name:String="",
        var isDefault:Boolean=false
    )
}