package uz.usoft.a24seven.network.di

import java.io.Serializable

class MockData (
)
{
    companion object {
        fun getProductList() : List<ProductObject>
        {
            var list= ArrayList<ProductObject>()
            list.add(ProductObject("","200"))
            list.add(ProductObject("","300"))
            list.add(ProductObject("","500"))
            list.add(ProductObject(""))
            list.add(ProductObject(""))
            return list
        }

        fun getPopularProductList() : List<ProductObject>
        {
            var list= ArrayList<ProductObject>()
            list.add(ProductObject("","500"))
            list.add(ProductObject("","600"))
            list.add(ProductObject("","500"))
            list.add(ProductObject(""))
            list.add(ProductObject(""))
            return list
        }

        fun getCategoriesList(): List<ProductObject>
        {
            var list= ArrayList<ProductObject>()
            list.add(ProductObject("Еда","200"))
            list.add(ProductObject("Одежда","300"))
            list.add(ProductObject("Телефоны и гаджеты","500"))
            list.add(ProductObject("Спорт товары"))
            list.add(ProductObject("Техника для кухни"))
            return list
        }

        fun getSubCategoriesList(): List<ProductObject>
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
    }

    data class ProductObject(
        val name:String="",
        val price:String=""
    ):Serializable

    data class FeedbackObject(
        val name:String=""
    )
}