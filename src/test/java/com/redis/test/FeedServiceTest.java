package com.redis.test;

import com.redis.model.FeedResponse;
import com.redis.model.Navigation;
import com.redis.repository.FeedRepository;
import com.redis.service.FeedServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedServiceTest {


    public static FeedServiceImpl feedService;

    @BeforeClass
    public static void beforeClass(){
        FeedRepository repository = mock(FeedRepository.class);
        when(repository.getCount()).thenReturn(10000l);
        when(repository.getByStartAndEnd(9999,9999)).thenReturn(Stream.of("el10000")
                .collect(Collectors.toSet()));
        when(repository.getByStartAndEnd(3,5)).thenReturn(Stream.of("el4", "el5", "el6")
                .collect(Collectors.toSet()));
        feedService = new FeedServiceImpl(repository);
    }

    @Test
    public void filterNegateTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = FeedServiceImpl.class.getDeclaredMethod("filterNegative", Long.class);
        method.setAccessible(true);

        assertEquals(((long) method.invoke(feedService, -1l)), 0l);
        assertEquals(((long) method.invoke(feedService, 2l)), 2l);
        assertEquals(((long) method.invoke(feedService, 0l)), 0l);
    }

    @Test
    public void filterNegativeWithErrorTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = FeedServiceImpl.class.getDeclaredMethod("filterNegativeWithError", Long.class);
        method.setAccessible(true);

        assertEquals(((long) method.invoke(feedService, 2l)), 2l);
        assertEquals(((long) method.invoke(feedService, 0l)), 0l);
    }

    @Test(expected = InvocationTargetException.class)
    public void filterNegativeWithErrorExceptionTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = FeedServiceImpl.class.getDeclaredMethod("filterNegativeWithError", Long.class);
        method.setAccessible(true);

        method.invoke(feedService, -1l);
    }

    @Test
    public void filterByCount() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = FeedServiceImpl.class.getDeclaredMethod("filterByMax", Long.class, Long.class);
        method.setAccessible(true);

        assertEquals(((long) method.invoke(feedService, -1l, 10l)), -1l);
        assertEquals(((long) method.invoke(feedService, 2l, 1l)), 1l);
        assertEquals(((long) method.invoke(feedService, 3l, 3l)), 3l);
    }

    @Test
    public void getNavigation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = FeedServiceImpl.class.getDeclaredMethod("getNavigation", Long.class, Long.class, Long.class);
        method.setAccessible(true);

        Navigation f = (Navigation) method.invoke(feedService, 0l, 10l, 9l);
        assertEquals(f, Navigation.builder()
                .hasNext(false)
                .hasPrev(false)
                .next("10")
                .prev("1")
                .build());

        Navigation f2 = (Navigation) method.invoke(feedService, 1l, 11l, 9l);
        assertEquals(f2, Navigation.builder()
                .hasNext(false)
                .hasPrev(true)
                .next("10")
                .prev("2")
                .build());

        Navigation f3 = (Navigation) method.invoke(feedService, 0l, 5l, 10l);
        assertEquals(f3, Navigation.builder()
                .hasNext(true)
                .hasPrev(false)
                .next("6")
                .prev("1")
                .build());

        Navigation f4 = (Navigation) method.invoke(feedService, 2l, 5l, 10l);
        assertEquals(f4, Navigation.builder()
                .hasNext(true)
                .hasPrev(true)
                .next("6")
                .prev("3")
                .build());
    }

    @Test
    public void testFeed(){
        FeedResponse errorResponse = feedService.getFeed(3l, 1l, 1l);
        assertEquals(errorResponse.getItems(), null);
        assertEquals(errorResponse.getNavigation(), null);

        FeedResponse response1 = feedService.getFeed(3l, 3l, null);
        assertEquals(response1.getItems().size(), 3);
        assertEquals(response1.getNavigation(), Navigation.builder()
                .hasNext(true)
                .hasPrev(true)
                .next("6")
                .prev("4")
                .build());

        FeedResponse response2 = feedService.getFeed(3l, 9999l, null);
        assertEquals(response2.getItems().size(), 1);
        assertEquals(response2.getNavigation(), Navigation.builder()
                .hasNext(false)
                .hasPrev(true)
                .next("10000")
                .prev("10000")
                .build());
    }
}