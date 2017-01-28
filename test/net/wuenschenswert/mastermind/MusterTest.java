package net.wuenschenswert.mastermind;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

import static net.wuenschenswert.mastermind.Farbe.*;

public class MusterTest {

  @Test
  public void testExtendedWith() throws Exception {
    assertEquals(new Muster(blau, grün), new Muster(blau).extendedWith(grün));
    assertEquals(new Muster(blau, grün, gelb), new Muster(blau, grün).extendedWith(gelb));
  }

  @Test
  public void testGetHistogram() throws Exception {
    Farbe[] farben = Farbe.values();
    assertArrayEquals(new int[]{1,0,0,0,0,0}, new Muster(farben[0]).getHistogram());
    assertArrayEquals(new int[]{0,1,0,0,0,0}, new Muster(farben[1]).getHistogram());
    assertArrayEquals(new int[]{0,0,0,0,0,1}, new Muster(farben[5]).getHistogram());
    assertArrayEquals(new int[]{0,0,3,0,0,1}, new Muster(farben[2],farben[5],farben[2],farben[2]).getHistogram());
  }

  @Test
  public void testAlleMuster() throws Exception {
    assertEquals(6, Muster.alleMuster(1).count());
    assertEquals(6*6, Muster.alleMuster(2).count());
    assertEquals(6*6*6, Muster.alleMuster(3).count());
    assertEquals(6*6*6*6, Muster.alleMuster(4).count());

    assertEquals(new HashSet<Muster>(Arrays.asList(
        new Muster(rot,rot),
        new Muster(rot,gelb),
        new Muster(rot,grün),
        new Muster(rot,blau),
        new Muster(rot,schwarz),
        new Muster(rot,weiß),
        new Muster(gelb,rot),
        new Muster(gelb,gelb),
        new Muster(gelb,grün),
        new Muster(gelb,blau),
        new Muster(gelb,schwarz),
        new Muster(gelb,weiß),
        new Muster(grün,rot),
        new Muster(grün,gelb),
        new Muster(grün,grün),
        new Muster(grün,blau),
        new Muster(grün,schwarz),
        new Muster(grün,weiß),
        new Muster(blau,rot),
        new Muster(blau,gelb),
        new Muster(blau,grün),
        new Muster(blau,blau),
        new Muster(blau,schwarz),
        new Muster(blau,weiß),
        new Muster(schwarz,rot),
        new Muster(schwarz,gelb),
        new Muster(schwarz,grün),
        new Muster(schwarz,blau),
        new Muster(schwarz,schwarz),
        new Muster(schwarz,weiß),
        new Muster(weiß,rot),
        new Muster(weiß,gelb),
        new Muster(weiß,grün),
        new Muster(weiß,blau),
        new Muster(weiß,schwarz),
        new Muster(weiß,weiß)
    )), Muster.alleMuster(2).collect(Collectors.toSet()));
  }
}