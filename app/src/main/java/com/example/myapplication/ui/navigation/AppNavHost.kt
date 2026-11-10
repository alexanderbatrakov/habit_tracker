package com.example.myapplication.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.core.designsystem.TestTags
import com.example.myapplication.di.DaggerViewModelFactory
import com.example.myapplication.feature.createhabit.CreateHabitEffect
import com.example.myapplication.feature.createhabit.CreateHabitScreen
import com.example.myapplication.feature.createhabit.CreateHabitViewModel
import com.example.myapplication.feature.edithabit.EditHabitEffect
import com.example.myapplication.feature.edithabit.EditHabitScreen
import com.example.myapplication.feature.edithabit.EditHabitViewModel
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogEffect
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogScreen
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogViewModel
import com.example.myapplication.feature.exercisecatalog.R as CatalogR
import com.example.myapplication.feature.habitdetail.HabitDetailEffect
import com.example.myapplication.feature.habitdetail.HabitDetailScreen
import com.example.myapplication.feature.habitdetail.HabitDetailViewModel
import com.example.myapplication.feature.habitslist.HabitsListEffect
import com.example.myapplication.feature.habitslist.HabitsListIntent
import com.example.myapplication.feature.habitslist.HabitsListScreen
import com.example.myapplication.feature.habitslist.HabitsListViewModel
import com.example.myapplication.feature.habitslist.R as HabitsR

private val topLevelRoutes = setOf(Destinations.HABITS_LIST, Destinations.EXERCISE_CATALOG)

@Composable
fun AppNavHost(
    viewModelFactory: DaggerViewModelFactory,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = { if (currentRoute in topLevelRoutes) AppBottomBar(currentRoute, navController) },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.HABITS_LIST,
            modifier = Modifier.padding(padding),
        ) {
            habitsListDestination(navController, viewModelFactory)
            exerciseCatalogDestination(navController, viewModelFactory)
            createHabitDestination(navController, viewModelFactory)
            habitDetailDestination(navController, viewModelFactory)
            editHabitDestination(navController, viewModelFactory)
        }
    }
}

@Composable
private fun AppBottomBar(
    currentRoute: String?,
    navController: NavHostController,
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Destinations.HABITS_LIST,
            onClick = { navController.navigateTopLevel(Destinations.HABITS_LIST) },
            icon = { Icon(Icons.Filled.List, contentDescription = null) },
            label = { Text(stringResource(HabitsR.string.habits_list_title)) },
            modifier = Modifier.testTag(TestTags.BottomBar.HABITS_TAB),
        )
        NavigationBarItem(
            selected = currentRoute == Destinations.EXERCISE_CATALOG,
            onClick = { navController.navigateTopLevel(Destinations.EXERCISE_CATALOG) },
            icon = { Icon(Icons.Filled.Search, contentDescription = null) },
            label = { Text(stringResource(CatalogR.string.exercise_catalog_tab)) },
            modifier = Modifier.testTag(TestTags.BottomBar.CATALOG_TAB),
        )
    }
}

private fun NavGraphBuilder.habitsListDestination(
    navController: NavHostController,
    viewModelFactory: DaggerViewModelFactory,
) {
    composable(Destinations.HABITS_LIST) {
        val vm: HabitsListViewModel = viewModel(factory = viewModelFactory)
        val state = vm.state.collectAsState().value
        LaunchedEffect(Unit) {
            vm.effects.collect { effect ->
                when (effect) {
                    HabitsListEffect.NavigateToCreateHabit ->
                        navController.navigate(Destinations.createHabit())
                    is HabitsListEffect.NavigateToHabitDetail ->
                        navController.navigate(Destinations.habitDetail(effect.habitId))
                    is HabitsListEffect.ToggleDoneRequested -> Unit
                }
            }
        }
        HabitsListScreen(
            state = state,
            onCreateHabitClick = { vm.dispatch(HabitsListIntent.CreateHabitClicked) },
            onToggleDone = { habitId -> vm.dispatch(HabitsListIntent.ToggleDoneToday(habitId)) },
            onHabitClick = { habitId -> vm.dispatch(HabitsListIntent.HabitClicked(habitId)) },
        )
    }
}

private fun NavGraphBuilder.exerciseCatalogDestination(
    navController: NavHostController,
    viewModelFactory: DaggerViewModelFactory,
) {
    composable(Destinations.EXERCISE_CATALOG) {
        val vm: ExerciseCatalogViewModel = viewModel(factory = viewModelFactory)
        val state = vm.state.collectAsState().value
        LaunchedEffect(Unit) {
            vm.start()
            vm.effects.collect { effect ->
                when (effect) {
                    is ExerciseCatalogEffect.CreateHabitRequested ->
                        navController.navigate(
                            Destinations.createHabit(
                                name = effect.exercise.name,
                                description = effect.exercise.description,
                            )
                        )
                    is ExerciseCatalogEffect.FetchFirstPage -> Unit
                    is ExerciseCatalogEffect.FetchNextPage -> Unit
                }
            }
        }
        ExerciseCatalogScreen(state = state, onIntent = vm::dispatch)
    }
}

private fun NavGraphBuilder.createHabitDestination(
    navController: NavHostController,
    viewModelFactory: DaggerViewModelFactory,
) {
    composable(
        route = Destinations.CREATE_HABIT,
        arguments = listOf(
            navArgument(Destinations.ARG_HABIT_NAME) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(Destinations.ARG_HABIT_DESCRIPTION) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
    ) { backStackEntry ->
        val prefillName = backStackEntry.arguments?.getString(Destinations.ARG_HABIT_NAME).orEmpty()
        val prefillDescription =
            backStackEntry.arguments?.getString(Destinations.ARG_HABIT_DESCRIPTION).orEmpty()
        val vm: CreateHabitViewModel = viewModel(factory = viewModelFactory)
        val state = vm.state.collectAsState().value
        LaunchedEffect(Unit) {
            vm.prefill(prefillName, prefillDescription)
            vm.effects.collect { effect ->
                when (effect) {
                    CreateHabitEffect.Saved ->
                        navController.popBackStack()
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

private fun NavGraphBuilder.habitDetailDestination(
    navController: NavHostController,
    viewModelFactory: DaggerViewModelFactory,
) {
    composable(
        route = Destinations.HABIT_DETAIL,
        arguments = listOf(
            navArgument(Destinations.ARG_HABIT_ID) { type = NavType.LongType },
        ),
    ) { backStackEntry ->
        val habitId = backStackEntry.arguments?.getLong(Destinations.ARG_HABIT_ID)
            ?: return@composable
        val vm: HabitDetailViewModel = viewModel(factory = viewModelFactory)
        val state = vm.state.collectAsState().value
        LaunchedEffect(habitId) {
            vm.start(habitId)
            vm.effects.collect { effect ->
                when (effect) {
                    HabitDetailEffect.NavigateBack ->
                        navController.popBackStack()
                    is HabitDetailEffect.NavigateToEdit ->
                        navController.navigate(Destinations.editHabit(effect.habitId))
                    is HabitDetailEffect.DeleteRequested -> Unit
                    is HabitDetailEffect.StatusUpdateRequested -> Unit
                }
            }
        }
        HabitDetailScreen(
            state = state,
            onIntent = vm::dispatch,
            onBack = { navController.popBackStack() },
        )
    }
}

private fun NavGraphBuilder.editHabitDestination(
    navController: NavHostController,
    viewModelFactory: DaggerViewModelFactory,
) {
    composable(
        route = Destinations.EDIT_HABIT,
        arguments = listOf(
            navArgument(Destinations.ARG_HABIT_ID) { type = NavType.LongType },
        ),
    ) { backStackEntry ->
        val habitId = backStackEntry.arguments?.getLong(Destinations.ARG_HABIT_ID)
            ?: return@composable
        val vm: EditHabitViewModel = viewModel(factory = viewModelFactory)
        val state = vm.state.collectAsState().value
        LaunchedEffect(habitId) {
            vm.start(habitId)
            vm.effects.collect { effect ->
                when (effect) {
                    EditHabitEffect.Saved ->
                        navController.popBackStack()
                    EditHabitEffect.NavigateBack ->
                        navController.popBackStack()
                    is EditHabitEffect.SaveRequested -> Unit
                }
            }
        }
        EditHabitScreen(
            state = state,
            onIntent = vm::dispatch,
            onBack = { navController.popBackStack() },
        )
    }
}

private fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
