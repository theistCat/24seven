package uz.usoft.a24seven.network.utils

import java.io.IOException

class NoConnectivityException : IOException(){
    override val message: String?
        get() = "Нет связи с сервером. \nВозможно отключена сеть!"
}