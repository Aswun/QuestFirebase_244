package com.example.prak14.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prak14.R
import com.example.prak14.modeldata.Siswa
import com.example.prak14.view.route.DestinasiDetail
import com.example.prak14.viewmodel.DetailViewModel
import com.example.prak14.viewmodel.PenyediaViewModel
import com.example.prak14.viewmodel.StatusUIDetail
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSiswaScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiDetail.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditItem(viewModel.statusUIDetail.let {
                    if (it is StatusUIDetail.Success) it.satusiswa?.id?.toInt() ?: 0 else 0
                }) },
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        val statusUI = viewModel.statusUIDetail
        DetailStatus(
            statusUIDetail = statusUI,
            retryAction = { viewModel.getSatuSiswa() },
            modifier = Modifier.padding(innerPadding),
            onDeleteClick = {
                // Logic Hapus
            }
        )
    }
}

@Composable
fun DetailStatus(
    statusUIDetail: StatusUIDetail,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    when (statusUIDetail) {
        is StatusUIDetail.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is StatusUIDetail.Success -> {
            if (statusUIDetail.satusiswa == null) {
                Box(modifier = modifier.fillMaxSize()) { Text("Data tidak ditemukan") }
            } else {
                ItemDetailSiswa(
                    siswa = statusUIDetail.satusiswa,
                    modifier = modifier.verticalScroll(rememberScrollState())
                )
            }
        }
        is StatusUIDetail.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun ItemDetailSiswa(
    siswa: Siswa,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponentDetailSiswa(labelResID = R.string.nama, value = siswa.nama)
            ComponentDetailSiswa(labelResID = R.string.alamat, value = siswa.alamat)
            ComponentDetailSiswa(labelResID = R.string.telpon, value = siswa.telpon)
        }
    }
}

@Composable
fun ComponentDetailSiswa(
    labelResID: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(bottom = 8.dp)) {
        Text(text = stringResource(labelResID), fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}