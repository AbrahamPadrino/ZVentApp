package com.example.z_ventapp.di

import android.content.Context
import androidx.room.Room
import com.example.z_ventapp.data.local.dao.ClienteDao
import com.example.z_ventapp.data.local.dao.ProductoDao
import com.example.z_ventapp.data.local.dao.TicketDao
import com.example.z_ventapp.data.local.dao.UsuarioDao
import com.example.z_ventapp.data.local.database.AppDatabase
import com.example.z_ventapp.data.repository.ClienteRepositoryImpl
import com.example.z_ventapp.data.repository.EmpresaRepositoryImpl
import com.example.z_ventapp.data.repository.ImpresoraRepositoryImpl
import com.example.z_ventapp.data.repository.ProductoRepositoryImpl
import com.example.z_ventapp.data.repository.TicketRepositoryImpl
import com.example.z_ventapp.data.repository.UsuarioRepositoryImpl
import com.example.z_ventapp.data.storage.LocalDataStore
import com.example.z_ventapp.domain.repository.ClienteRepository
import com.example.z_ventapp.domain.repository.EmpresaRepository
import com.example.z_ventapp.domain.repository.ImpresoraRepository
import com.example.z_ventapp.domain.repository.ProductoRepository
import com.example.z_ventapp.domain.repository.TicketRepository
import com.example.z_ventapp.domain.repository.UsuarioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "CursoPv"

    //**** Proveer la db **** //
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    //**** Proveer localdatastore **** //
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = LocalDataStore(context)

    //**** Proveer el DAO **** //
    @Singleton
    @Provides
    fun provideUsuarioDao(db: AppDatabase) = db.usuarioDao()

    @Singleton
    @Provides
    fun provideClienteDao(db: AppDatabase) = db.clienteDao()

    @Singleton
    @Provides
    fun provideProductoDao(db: AppDatabase) = db.productoDao()

    @Singleton
    @Provides
    fun provideTicketDao(db: AppDatabase) = db.ticketDao()

    //**** Proveer el Repository **** //
    @Singleton
    @Provides
    fun provideUsuarioRepository(dao: UsuarioDao) : UsuarioRepository {
        return UsuarioRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideClienteRepository(dao: ClienteDao) : ClienteRepository {
        return ClienteRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideProductoRepository(dao: ProductoDao) : ProductoRepository {
        return ProductoRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideTicketRepository(dao: TicketDao) : TicketRepository {
        return TicketRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideEmpresaRepository(dataStore: LocalDataStore) : EmpresaRepository {
        return EmpresaRepositoryImpl(dataStore)
    }

    @Singleton
    @Provides
    fun provideImpresoraRepository(dataStore: LocalDataStore): ImpresoraRepository {
        return ImpresoraRepositoryImpl(dataStore)
    }

}