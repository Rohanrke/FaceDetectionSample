package com.rohan.face.detection.data.repo

import android.net.Uri
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.rohan.face.detection.data.dao.FeedDao
import com.rohan.face.detection.data.entity.FeedEntity
import com.rohan.face.detection.domain.EntityToModelMapper
import com.rohan.face.detection.domain.ModelToEntityMapper
import com.rohan.face.detection.domain.model.FeedModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoFeedRepoImplTest {

    @Mock
    private lateinit var dao: FeedDao

    @Mock
    private lateinit var mockedUri: Uri

    @Mock
    private lateinit var entityMapper: ModelToEntityMapper<FeedModel, FeedEntity>

    @Mock
    private lateinit var modelMapper: EntityToModelMapper<FeedEntity, FeedModel>

    private lateinit var repo: PhotoFeedRepoImpl

    @Before
    fun setup() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this)

        // Set the main dispatcher for coroutine testing
        Dispatchers.setMain(Dispatchers.Unconfined)

        // Create an instance of PhotoFeedRepoImpl with mocked dependencies
        repo = PhotoFeedRepoImpl(dao, entityMapper, modelMapper)
    }

    @Test
    fun `getAllFeed should return mapped list of FeedModels`() = runTest {
        // Given
        val feedEntityList = listOf(
            FeedEntity(mockedUri, 1234567890),
            FeedEntity(mockedUri, 1234567891)
        )
        val expectedFeedModels = listOf(
            FeedModel(uri = mockedUri, lastUpdatedTimeStamp = 1234567890),
            FeedModel(uri = mockedUri, lastUpdatedTimeStamp = 1234567891)
        )

        // Mocking dao call
        whenever(dao.getAllImages()).thenReturn(feedEntityList)
        whenever(modelMapper.map(feedEntityList[0])).thenReturn(expectedFeedModels[0])
        whenever(modelMapper.map(feedEntityList[1])).thenReturn(expectedFeedModels[1])

        // When
        val result = repo.getAllFeed()

        // Then
        assertEquals(expectedFeedModels, result)
        verify(dao).getAllImages()  // Verifying dao method was called
        verify(modelMapper).map(feedEntityList[0])  // Verify mapping of the first entity
        verify(modelMapper).map(feedEntityList[1])  // Verify mapping of the second entity
    }

    @Test
    fun `updateFeed should call dao update method`() = runTest {
        // Given
        val feedModel = FeedModel(uri = mockedUri, lastUpdatedTimeStamp = 1234567890)
        val feedEntity = FeedEntity(mockedUri, 1234567890)

        // Mocking the mapper behavior
        whenever(entityMapper.map(feedModel)).thenReturn(feedEntity)

        // When
        repo.updateFeed(feedModel)

        // Then
        verify(dao).updateImage(feedEntity)  // Verify dao update method is called with the correct entity
        verify(entityMapper).map(feedModel)  // Verify that the model was mapped to an entity
    }

    @After
    fun tearDown() {
        // Clean up after tests
        Dispatchers.resetMain()  // Reset the dispatcher after the test
    }
}
