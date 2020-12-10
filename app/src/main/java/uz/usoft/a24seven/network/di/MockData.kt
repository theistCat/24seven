package uz.usoft.a24seven.network.di

class MockData (
)
{
    companion object {
        fun getProductList() : List<ProductObject>
        {
            var list= ArrayList<ProductObject>()
            list.add(ProductObject(""))
            list.add(ProductObject(""))
            list.add(ProductObject(""))
            list.add(ProductObject(""))
            list.add(ProductObject(""))
            return list
        }
    }

    data class ProductObject(
        val name:String=""
    )
}