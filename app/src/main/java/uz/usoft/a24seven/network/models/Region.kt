package uz.usoft.a24seven.network.models

data class Region (
    val id: Int,
    val name:String,
    val cities:List<Region>?
){
    override fun equals(other: Any?): Boolean {
        if(other is Region)
        {
            return this.id==other.id
        }
        else
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }
}

data class RegionResponse(
    val regions:List<Region>
)