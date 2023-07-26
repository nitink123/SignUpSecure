package com.example.signupsecure

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.signupsecure.databinding.ActivityLogoutBinding

class LogoutActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        mBinding.btnLogout.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnLogout -> {
                // on click of logout button redirect to signup page
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
        }
    }
}