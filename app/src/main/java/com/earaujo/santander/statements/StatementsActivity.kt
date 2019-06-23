package com.earaujo.santander.statements

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.earaujo.santander.R
import com.earaujo.santander.repository.models.StatementsListModel
import com.earaujo.santander.repository.models.UserAccountModel
import kotlinx.android.synthetic.main.activity_statements.*
import java.lang.ref.WeakReference

class StatementsActivity : AppCompatActivity(), StatementsActivityInput {

    lateinit var statementsInteractorInput: StatementsInteractorInput
    lateinit var statementsRouter: StatementsRouter
    private lateinit var adapter: StatementsListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statements)

        setupHomeActivity()

        statementsInteractorInput.fetchUserData(intent)
        statementsInteractorInput.fetchStatements()
    }

    fun setupHomeActivity() {
        statementsRouter = StatementsRouter()
        statementsRouter.activity = WeakReference(this)

        statementsInteractorInput = StatementsInteractor().also {
            it.statementsPresenterInput = StatementsPresenter().also {
                it.statementsActivityInput = WeakReference(this)
            }
        }

        btn_logout.setOnClickListener {
            statementsRouter.logout()
        }
    }

    override fun displayUserData(userAccount: UserAccountModel) {
        tv_username.text = userAccount.name
        tv_account.text = userAccount.bankAccount
        tv_balance.text = userAccount.balance.toString()
    }

    override fun displayStatementsData(statementList: List<StatementsListModel>) {
        adapter = StatementsListAdapter(statementList)
        linearLayoutManager = LinearLayoutManager(this)
        rv_statements.layoutManager = linearLayoutManager
        rv_statements.adapter = adapter
        nsv_statements.post{ nsv_statements.scrollTo(0, 0) }
    }

    companion object {
        val INTENT_USER_DATA = "user_data"
    }

}

interface StatementsActivityInput {
    fun displayUserData(userAccount: UserAccountModel)
    fun displayStatementsData(statementList: List<StatementsListModel>)
}