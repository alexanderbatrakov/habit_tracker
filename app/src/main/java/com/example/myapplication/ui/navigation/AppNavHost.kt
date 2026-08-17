package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.di.DaggerViewModelFactory
import com.example.myapplication.feature.createhabit.CreateHabitEffect
import com.example.myapplication.feature.createhabit.CreateHabitViewModel
import com.example.myapplication.feature.habitslist.HabitsListEffect
import com.example.myapplication.feature.habitslist.HabitsListViewModel
import com.example.myapplication.ui.screen.CreateHabitScreen
import com.example.myapplication.ui.screen.HabitsListScreen

@Composable
fun AppNavHost(
    viewModelFactory: DaggerViewModelFactory,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.HABITS_LIST,
    ) {
        composable(Destinations.HABITS_LIST) {
            val vm: HabitsListViewModel = viewModel(factory = viewModelFactory)
            val state = vm.state.collectAsState().value
            LaunchedEffect(Unit) {
                vm.effects.collect { effect ->
                    when (effect) {
                        HabitsListEffect.NavigateToCreateHabit ->
                            navController.navigate(Destinations.CREATE_HABIT)
                        is HabitsListEffect.ToggleDoneRequested -> Unit
                    }
                }
            }
            HabitsListScreen(
                state = state,
                onCreateHabitClick = { vm.dispatch(com.example.myapplication.feature.habitslist.HabitsListIntent.CreateHabitClicked) },
                onToggleDone = { habitId -> vm.dispatch(com.example.myapplication.feature.habitslist.HabitsListIntent.ToggleDoneToday(habitId)) },
            )
        }

        composable(Destinations.CREATE_HABIT) {
            val vm: CreateHabitViewModel = viewModel(factory = viewModelFactory)
            val state = vm.state.collectAsState().value
            LaunchedEffect(Unit) {
                vm.effects.collect { effect ->
                    when (effect) {
                        CreateHabitEffect.Saved -> navController.popBackStack()
                        is CreateHabitEffect.SaveRequested -> Unit
                    }
                }
            }
            CreateHabitScreen(
                state = state,
                onIntent = vm::dispatch,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
