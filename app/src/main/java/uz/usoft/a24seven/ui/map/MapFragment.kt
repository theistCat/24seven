package uz.usoft.a24seven.ui.map

import android.content.Context
import android.content.IntentSender
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import uz.usoft.a24seven.R
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.logo.Alignment
import com.yandex.mapkit.logo.HorizontalAlignment
import com.yandex.mapkit.logo.VerticalAlignment
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.databinding.FragmentMapBinding
import uz.usoft.a24seven.network.models.LocPoint
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.cart.CartFragmentDirections
import uz.usoft.a24seven.ui.checkout.CheckOutFragmentDirections
import uz.usoft.a24seven.ui.profile.myAddresses.AddressListFragmentDirections
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.show
import uz.usoft.a24seven.utils.showSnackbar

class MapFragment : Fragment(), CameraListener {

    private val mapViewModel: MapViewModel by viewModel()

    private val safeArgs: MapFragmentArgs by navArgs()

    val handler = Handler()
    lateinit var runnable:Runnable

    private var _binding:FragmentMapBinding?=null
    private val binding get() = _binding!!

    var chosenAddress=""
    var chosenAddressName=""
    var chosenAddressNumber=""
    var chosenAddressCity=""
    var chosenAddressDistrict=""
    var chosenAddressComment=""

    var lat=""
    var long=""
    var city=""
    var region=""
    var addresss=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chosenAddressName=it.getString("chosenAddressName")?:""
            chosenAddressNumber=it.getString("chosenAddressNumber")?:""
            chosenAddressCity=it.getString("chosenAddressCity")?:""
            chosenAddressDistrict=it.getString("chosenAddressDistrict")?:""
            chosenAddressComment=it.getString("chosenAddressComment")?:""
        }

        MapKitFactory.initialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMapBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val alignment = Alignment(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
        binding.mapView.map.logo.setAlignment(alignment)

        val mapView=binding.mapView
        mapView.map.move(
            CameraPosition( Point(41.311081, 69.240562),14.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH,0f),null
        )

        mapViewModel.addressData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {

                        binding.btnChooseLocation.isEnabled = false
                        binding.findMe.isEnabled = false
                    }
                    is Resource.Success -> {
                        val address = resource.data.response.getAsJsonObject("GeoObjectCollection")
                            .getAsJsonArray("featureMember")[0]
                        if (address.isJsonObject) {
                            val gson = (address as JsonObject).getAsJsonObject("GeoObject")
                                .getAsJsonObject("metaDataProperty")
                                .getAsJsonObject("GeocoderMetaData").get("text")

                            Log.i("mapkitlocation", gson.toString())
                            val addressArray = gson.asString.split(",")

                            city = addressArray[1]

                            if (addressArray.size > 3) {
                                region = addressArray[2]
                                addresss = addressArray.subList(3, addressArray.size).joinToString()
                            }
                            else {
                                addresss = addressArray[2]

                                val temp =
                                    resource.data.response.getAsJsonObject("GeoObjectCollection")
                                        .getAsJsonArray("featureMember")

                                if (temp.isJsonArray) {
                                    temp.forEach {
                                        if (it.isJsonObject) {
                                            val tempAddress =
                                                (it as JsonObject).getAsJsonObject("GeoObject")
                                                    .getAsJsonObject("metaDataProperty")
                                                    .getAsJsonObject("GeocoderMetaData")
                                                    .getAsJsonObject("Address")

                                            tempAddress.getAsJsonArray("Components")
                                                .forEach { component  ->
                                                    component as JsonObject
                                                    if (component.get("kind").asString == "district") {
                                                        region = component.get("name").asString
                                                    }

                                                }

                                        }
                                        if(region.isNotBlank())
                                            return@forEach

                                    }
                                }
                            }

                            chosenAddress = gson.toString()

                            binding.streetHouseText.text = chosenAddress


                            binding.streetHouseText.visibility = View.INVISIBLE

                            binding.streetHouseText.setTextColor(Color.parseColor("#000000"))
                            binding.cityText.visibility = View.INVISIBLE
                            binding.streetHouseText.show()

                            binding.btnChooseLocation.isEnabled = true
                            binding.findMe.isEnabled = true
                        }

                    }
                    is Resource.GenericError -> {

                        binding.btnChooseLocation.isEnabled = false
                        binding.findMe.isEnabled = true
                        Log.i(
                            "mapkitlocation",
                            resource.errorResponse.jsonResponse.getString("error")
                        )
                        showSnackbar(resource.errorResponse.jsonResponse.getString("error"))
                    }
                    is Resource.Error -> {

                        binding.btnChooseLocation.isEnabled = false
                        binding.findMe.isEnabled = true
                        resource.exception.message?.let { it1 -> showSnackbar(it1) }

                        resource.exception.message?.let { it1 -> Log.i("mapkitlocation", it1) }
                    }
                }
            }
        })
        mapView.map.addCameraListener(this)

        binding.mapBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.findMe.setOnClickListener {
            if(checkIfGpsEnabled())
                findMe()
            else
                requestLocationSettingsOn()
        }

        binding.btnChooseLocation.setOnClickListener {
            when(safeArgs.fromDestination){
                Variables.fromCheckout-> {
                val action = CartFragmentDirections.actionNavCartToNavCheckOut(
                    safeArgs.checkOutData!!,
                    addresss,
                    region,
                    city,
                    LocPoint(lat, long)
                )

                    findNavController().popBackStack(R.id.nav_checkOut,true)
                navigate(action)
            }
                Variables.fromEditAddress-> {
                    val action = AddressListFragmentDirections.actionNavAddressListToSelectedAddressFragment(
                        safeArgs.addressId,
                        addresss,
                        region,
                        city,
                        LocPoint(lat, long)
                    )
                    findNavController().popBackStack(R.id.nav_selectedAddress,true)
                    navigate(action)
                }
            else->{
                val action = AddressListFragmentDirections.actionNavAddressListToNavAddAddress(addresss,region,city,
                    LocPoint(lat, long))


                findNavController().popBackStack(R.id.nav_addAddress,true)
                navigate(action)

            }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val mainActivity=requireActivity() as MainActivity

        mainActivity.hideToolbar()
        mainActivity.hideBottomNavigation()

    }

    fun findMe(){
        MapKitFactory.getInstance().createLocationManager().requestSingleUpdate(object :
            com.yandex.mapkit.location.LocationListener {
            override fun onLocationUpdated(location: com.yandex.mapkit.location.Location) {
                binding.mapView?.map?.move(
                    CameraPosition( Point(location.position.latitude, location.position.longitude),14.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH,0.5f),null
                )
            }
            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                Log.d("TagCheck", locationStatus.name)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    private fun checkIfGpsEnabled(): Boolean {
        val lm: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun requestLocationSettingsOn() {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(createLocationRequest()!!)

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            // All loc_pin settings are satisfied. The client can initialize
            // loc_pin requests here.
            findMe()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        requireActivity(),
                        1001
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.warning_enable_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    private fun createLocationRequest(): LocationRequest? {
        return LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        updateReason: CameraUpdateReason,
        finishedChanging: Boolean
    ) {
        try {
            if (finishedChanging) {
                runnable = object : Runnable {
                    override fun run() {
                        if(finishedChanging) {
                            lat = cameraPosition.target.latitude.toString()
                            long = cameraPosition.target.longitude.toString()
                            mapViewModel.getAddressData(
                                Variables.yandexKeySecond,
                                long,
                                lat
                            )
                        }
                    }
                }
                handler.postDelayed(runnable, 1500)

            } else {
                binding.streetHouseText.visibility=View.VISIBLE
                binding. streetHouseText.text = getString(R.string.detecting_address)
                binding.streetHouseText.setTextColor(Color.parseColor("#b6b6b6"))
                binding.cityText.visibility = View.INVISIBLE
                binding.btnChooseLocation.isEnabled = false

                handler.removeCallbacks(runnable)

            }
        } catch (ex: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

}