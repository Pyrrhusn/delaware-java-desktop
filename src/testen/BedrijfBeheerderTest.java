package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.Bedrijf;
import domein.BedrijfBeheerder;
import domein.BedrijfVerandering;
import domein.IBedrijfVerandering;
import javafx.collections.ObservableList;
import repository.BedrijfDao;

@ExtendWith(MockitoExtension.class)
public class BedrijfBeheerderTest {

    @Mock
    private BedrijfDao bedrijfDao;

    @InjectMocks
    private BedrijfBeheerder bedrijfBeheerder;


    @Test
    public void testGetBedrijvenMetAanVragenVoorWijzingen() {
       
        List<Bedrijf> bedrijven = Arrays.asList(mock(Bedrijf.class), mock(Bedrijf.class));
        List<BedrijfVerandering> veranderingen = Arrays.asList(mock(BedrijfVerandering.class), mock(BedrijfVerandering.class));
        
        for (Bedrijf bedrijf : bedrijven) {
            when(bedrijf.getVeranderingen()).thenReturn(veranderingen);
        }
        when(bedrijfDao.getBedrijvenMetAanVragenVoorWijzingen()).thenReturn(bedrijven);

     
        ObservableList<IBedrijfVerandering> result = bedrijfBeheerder.getBedrijvenMetAanVragenVoorWijzingen();

       
        assertEquals(veranderingen.size() * bedrijven.size(), result.size());
    }

    @Test
    public void testGetSpecifiekBedrijf() {
        Bedrijf bedrijf = mock(Bedrijf.class);
        when(bedrijf.getId()).thenReturn(1);
        when(bedrijfDao.get(1)).thenReturn(bedrijf);
        Bedrijf result = bedrijfBeheerder.getSpecifiekBedrijf(bedrijf);  
        assertEquals(bedrijf, result);
    }

    @Test
    public void testVoegBedrijfToe() {
        Bedrijf bedrijf = mock(Bedrijf.class);
        bedrijfBeheerder.voegBedrijfToe(bedrijf);
        verify(bedrijfDao).voegBedrijfToe(bedrijf);
    ;
    }

    @Test
    public void testUpdateBedrijf() {
        Bedrijf bedrijf = mock(Bedrijf.class);
        bedrijfBeheerder.updateBedrijf(bedrijf);
        verify(bedrijfDao).updateBedrijf(bedrijf);
    }
}
