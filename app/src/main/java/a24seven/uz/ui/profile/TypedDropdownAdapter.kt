package a24seven.uz.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import a24seven.uz.R
import a24seven.uz.network.models.Region

class TypedDropdownAdapter(context: Context, val resource:Int, val list: List<Region>): ArrayAdapter<Region>(context,resource,list) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view= convertView
        if(convertView==null)
           view= LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_lay,parent,false)

        val title=view?.findViewById<TextView>(R.id.spinner_text)

//        view?.setOnClickListener {
//            Toast.makeText(context, getItem(position)?.id.toString(), Toast.LENGTH_SHORT).show()
//        }

        if(list.isEmpty())
            title?.text = "Nothing Found"

        title?.text =  getItem(position)?.name

        return view!!
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as Region).name
            }

            override fun performFiltering(p0: CharSequence?): FilterResults {
                //TODO("Not yet implemented")
                return FilterResults()
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                //TODO("Not yet implemented")
            }

        }
    }


    fun setSelection(id:Int):String{
        val index=list.indexOf(Region(id,"", arrayListOf()))
        if (index!=-1)
            return getItem(index)?.name?:""
        else return ""
    }

    fun getSelection(id:Int): Region?{
        val index=list.indexOf(Region(id,"", arrayListOf()))
        if (index!=-1)
            return getItem(index)
        else return null
    }
}