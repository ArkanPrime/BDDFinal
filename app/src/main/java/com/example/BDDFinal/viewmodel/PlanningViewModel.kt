package com.example.BDDFinal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BDDFinal.data.AppDatabase
import com.example.BDDFinal.data.Planning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlanningViewModel(application: Application) : AndroidViewModel(application) {
    private val planningDao = AppDatabase.getDatabase(application).planningDao()


    private val _planning = MutableStateFlow<Planning?>(null)
    val planning: StateFlow<Planning?> get() = _planning

    fun loadPlanning(userId: Int) {
        viewModelScope.launch {
            val plans = planningDao.getPlanningForUser(userId)
            _planning.value = plans.lastOrNull()
        }
    }

    fun savePlanning(userId: Int, slot1: String, slot2: String, slot3: String, slot4: String, date: String? = null) {
        viewModelScope.launch {
            val existing = planningDao.getPlanningForUser(userId).lastOrNull()
            if (existing == null) {
                val newPlan = Planning(
                    userId = userId,
                    slot1 = slot1,
                    slot2 = slot2,
                    slot3 = slot3,
                    slot4 = slot4,
                    date = date
                )
                planningDao.insertPlanning(newPlan)
            } else {
                val updatedPlan = existing.copy(
                    slot1 = slot1,
                    slot2 = slot2,
                    slot3 = slot3,
                    slot4 = slot4,
                    date = date
                )
                planningDao.updatePlanning(updatedPlan)
            }
            loadPlanning(userId)
        }
    }
}
