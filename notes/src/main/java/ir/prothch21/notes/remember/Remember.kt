package ir.prothch21.notes.remember

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import carbon.widget.FrameLayout
import carbon.widget.TextView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import ir.huri.jcal.JalaliCalendar
import ir.protech21.ExpandableLinearLayout
import ir.protech21.NumberChoicer
import ir.protech21.bottomsheet.BottomSheetFragment
import ir.protech21.persian.date.DatePickerDialog
import ir.protech21.persian.time.TimePickerDialog
import ir.prothch21.notes.R
import okhttp3.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class Remember : AppCompatActivity(), View.OnClickListener {
    var looping = arrayOf("ساعتی", "روزانه", "هفتگی", "ماهانه", "سالانه")
    var phone: String? = ""
    var token: String? = ""
    var update = false
    var code: String? = null
    var now: JalaliCalendar? = null
    var hashMap = HashMap<String, File>()
    private var cir: FrameLayout? = null
    private var cart: FrameLayout? = null
    private var titleAlarm: EditText? = null
    private var dateButton: TextView? = null
    private var remember: CheckBox? = null
    private var timeExpandableLinearLayout: ExpandableLinearLayout? = null
    private var timeAlarm: TextView? = null
    private var loop: CheckBox? = null
    private var loopLin: ExpandableLinearLayout? = null
    private var alarmHowRepet: android.widget.TextView? = null
    private var alarmNumberRepet: NumberChoicer? = null
    private var alarmPriodRepet: NumberChoicer? = null
    private var alarmPlace: EditText? = null
    private var explaneAlarm: EditText? = null
    private var notify: CheckBox? = null
    private var sms: CheckBox? = null
    private var apply: android.widget.TextView? = null
    private var title: FrameLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remember2)
        initView()
        apply!!.setOnClickListener(this)
        alarmHowRepet!!.setOnClickListener { view: View -> Loop(view) }
        update = intent.getBooleanExtra("update", false)
        remember!!.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                timeExpandableLinearLayout!!.show()
            } else {
                timeExpandableLinearLayout!!.hide()
            }
        }
        loop!!.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                loopLin!!.show()
            } else {
                loopLin!!.hide()
            }
        }
        now = JalaliCalendar()
        //        numberPicker.setMinValue(0);
//        numberPicker.setMaxValue(strings.length-1);
//        numberPicker.setDisplayedValues(strings);
//        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> Toast.makeText(this, strings[newVal], Toast.LENGTH_SHORT).show());
        dateButton!!.text = String.format(" %s  %s ", now!!.dayOfWeekDayMonthString, now!!.year)
        dateButton!!.setOnClickListener { v: View? ->
            val datePickerDialog = DatePickerDialog
                    .newInstance({ view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        now!![year, monthOfYear + 1] = dayOfMonth
                        dateButton!!.text = String.format("%s %s %s %s", now!!.dayOfWeekString, now!!.day, now!!.monthString, now!!.year)
                    }, now!!.year, now!!.month - 1, now!!.day)
            datePickerDialog.isThemeDark = true
            datePickerDialog.show(this@Remember.fragmentManager, "tpd")
        }
        remember!!.typeface = Typeface.createFromAsset(assets, "fonts/iransans.ttf")
        //        remember.setOnCheckedChangeListener((buttonView, isChecked) -> TopDrawer(materialCardView, isChecked?0:materialCardView.getLayoutParams().height, isChecked?200:0));
        timeAlarm!!.setOnClickListener { v: View -> setTime(v) }
        loop!!.typeface = Typeface.createFromAsset(assets, "fonts/iransans.ttf")
        //        loop.setOnCheckedChangeListener((buttonView, isChecked) -> TopDrawer(loopLin, isChecked?0:loopLin.getLayoutParams().height, isChecked?200:0));
        sms!!.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean -> phone = if (isChecked) getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("phone", null) else "" }
        notify!!.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean -> token = if (isChecked) getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("token", null) else "" }
        if (update) {
            titleAlarm!!.setText(intent.getStringExtra("title"))
            timeAlarm!!.text = String.format("%s:%s", intent.getStringExtra("hour"), intent.getStringExtra("minute"))
            dateButton!!.text = intent.getStringExtra("date")
            alarmPlace!!.setText(intent.getStringExtra("location"))
            explaneAlarm!!.setText(intent.getStringExtra("des"))
            code = intent.getStringExtra("code")
        }
    }

    var position = -1
    private fun Loop(view: View) {
        val bottomSheetFragment = BottomSheetFragment.newInstance(looping.toCollection(ArrayList()), "نوع تکرار را مشخص کنید") {
            position = it
            (view as TextView).text = looping[position];
        };
        bottomSheetFragment.show(getSupportFragmentManager(), "loop");
    }

    private fun setTime(v: View) {
        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int -> timeAlarm!!.text = String.format("%d:%d", hourOfDay, minute) }, 0, 0, true)
        timePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
        timePickerDialog.show()
        timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorgold))
        timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorgold))
    }

    override fun onClick(v: View) {
        if (v.id == R.id.apply) {
            if (titleAlarm!!.text.toString().isEmpty()) {
                titleAlarm!!.error = "لطفا این کادر را پر کنید"
            } else {
                val pd = alarmPriodRepet!!.text.toString()
                val anr = alarmNumberRepet!!.text.toString()
                if (update) {
                    update(pd, anr, getString(R.string.url) + "updateRemember/")
                } else {
                    send(pd, anr, getString(R.string.url) + "remember/")
                }
            }
        }
    }

    private fun update(pd: String, anr: String, url: String) {
        val dialog = ProgressDialog(this)
        dialog.setMessage("درحال بارگذاری...")
        dialog.isIndeterminate = false
        dialog.max = 100
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.progress = 0
        dialog.show()
        AndroidNetworking.upload(url)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .addMultipartParameter("title", titleAlarm!!.text.toString())
                .addMultipartParameter("id", getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("id", null))
                .addMultipartParameter("year", now!!.year.toString())
                .addMultipartParameter("month", now!!.month.toString())
                .addMultipartParameter("day", now!!.day.toString())
                .addMultipartParameter("code", code)
                .addMultipartParameter("priod", pd)
                .addMultipartParameter("hour", timeAlarm!!.text.toString().substring(0, timeAlarm!!.text.toString().indexOf(":")))
                .addMultipartParameter("minute", timeAlarm!!.text.toString().substring(timeAlarm!!.text.toString().indexOf(":") + 1)) //                .addMultipartParameter("TypeLoop", String.valueOf(alarmHowRepet.getSelectedItemPosition()))
                .addMultipartParameter("loop", anr)
                .addMultipartParameter("location", alarmPlace!!.text.toString())
                .addMultipartParameter("des", explaneAlarm!!.text.toString())
                .addMultipartParameter("sms", phone).addMultipartParameter("notify", token).build().setUploadProgressListener { bytesUploaded, totalBytes ->
                    dialog.progress = (bytesUploaded / (totalBytes.toInt() / 100)).toInt() //Updating progress
                    dialog.setTitle(dialog.progress.toString())
                }.getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                    override fun onResponse(okHttpResponse: Response, response: String) {
                        dialog.dismiss()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onError(anError: ANError) {
                        dialog.dismiss()
                        Toast.makeText(this@Remember, "خطا در اتصال", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun send(pd: String, anr: String, url: String) {
        val dialog = ProgressDialog(this)
        dialog.setMessage("درحال بارگذاری...")
        dialog.isIndeterminate = false
        dialog.max = 100
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.progress = 0
        dialog.show()
        AndroidNetworking.upload(url).addMultipartFile(hashMap)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .addMultipartParameter("title", titleAlarm!!.text.toString())
                .addMultipartParameter("id", getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("id", null))
                .addMultipartParameter("year", now!!.year.toString())
                .addMultipartParameter("month", now!!.month.toString())
                .addMultipartParameter("day", now!!.day.toString())
                .addMultipartParameter("priod", pd)
                .addMultipartParameter("hour", timeAlarm!!.text.toString().substring(0, timeAlarm!!.text.toString().indexOf(":")))
                .addMultipartParameter("minute", timeAlarm!!.text.toString().substring(timeAlarm!!.text.toString().indexOf(":") + 1))
                .addMultipartParameter("TypeLoop", position.toString())
                .addMultipartParameter("loop", anr)
                .addMultipartParameter("location", alarmPlace!!.text.toString())
                .addMultipartParameter("des", explaneAlarm!!.text.toString())
                .addMultipartParameter("sms", phone)
                .addMultipartParameter("notify", token).build().setUploadProgressListener { bytesUploaded, totalBytes ->
                    dialog.progress = (bytesUploaded / (totalBytes.toInt() / 100)).toInt() //Updating progress
                    dialog.setTitle(dialog.progress.toString())
                }.getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                    override fun onResponse(okHttpResponse: Response, response: String) {
                        dialog.dismiss()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onError(anError: ANError) {
                        dialog.dismiss()
                        Toast.makeText(this@Remember, "خطا در اتصال", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun initView() {
        cir = findViewById(R.id.cir)
        cart = findViewById(R.id.cart)
        titleAlarm = findViewById(R.id.title_alarm)
        dateButton = findViewById(R.id.dateButton)
        remember = findViewById(R.id.remember)
        timeExpandableLinearLayout = findViewById(R.id.timeExpandableLinearLayout)
        timeAlarm = findViewById(R.id.time_alarm)
        loop = findViewById(R.id.loop)
        loopLin = findViewById(R.id.loopLin)
        alarmHowRepet = findViewById(R.id.alarm_how_repet)
        alarmNumberRepet = findViewById(R.id.alarm_number_repet)
        alarmPriodRepet = findViewById(R.id.alarm_priod_repet)
        alarmPlace = findViewById(R.id.alarm_place)
        explaneAlarm = findViewById(R.id.explane_alarm)
        notify = findViewById(R.id.notify)
        sms = findViewById(R.id.sms)
        apply = findViewById(R.id.apply)
        title = findViewById(R.id.title)
    }

    fun Finish(view: View?) {
        finish()
    }
}