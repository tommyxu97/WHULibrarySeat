package com.xht97.whulibraryseat;

import com.xht97.whulibraryseat.model.bean.SeatTime;
import com.xht97.whulibraryseat.util.AppDataUtil;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void seatTimeTest() {
        List<SeatTime> list = AppDataUtil.getFullSeatTime();
        System.out.print(list);
    }
}