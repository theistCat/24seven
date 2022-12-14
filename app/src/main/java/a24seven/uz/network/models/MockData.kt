package a24seven.uz.network.models

import android.content.Context
import a24seven.uz.R

class MockData ()
{
    companion object {
        var _context: Context?=null
        val context get() = _context!!
        fun getPaymentMethodList(context:Context): List<PaymentMethodObject>
        {
            _context =context
            val list= ArrayList<PaymentMethodObject>()
            list.add(PaymentMethodObject(context.getString(R.string.cash)))
            list.add(PaymentMethodObject(context.getString(R.string.transfer)))
            return list
        }
    }

    data class PaymentMethodObject(
        val name:String="",
        var isDefault:Boolean=false
    )

}