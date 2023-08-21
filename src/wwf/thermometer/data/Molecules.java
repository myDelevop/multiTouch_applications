package wwf.thermometer.data;

import java.util.EnumMap;

import org.mt4j.MTApplication;

import wwf.thermometer.ThermometerScene;
import wwf.thermometer.util.WaterStatus;

// TODO: Auto-generated Javadoc
/**
 *  Associa ad ogni stato dell'acqua una molecola.
 */
public class Molecules {
    
    /** The molecules. */
    private EnumMap<WaterStatus, Molecule> molecules;
    
    /**
     * Instantiates a new images.
     *
     * @param pa the pa
     */
    public Molecules(final MTApplication pa, final ThermometerScene therm) { 
        
        molecules = new EnumMap<WaterStatus, Molecule>(WaterStatus.class);

        molecules.put(WaterStatus.ICE, new Molecule(pa, therm, WaterStatus.ICE));
        molecules.put(WaterStatus.WATER, new Molecule(pa, therm, WaterStatus.WATER));
        molecules.put(WaterStatus.VAPOR, new Molecule(pa, therm, WaterStatus.VAPOR));

    }

    /**
     * Gets the molecules.
     *
     * @return the molecules
     */
    public EnumMap<WaterStatus, Molecule> getMolecules() {
        return molecules;
    }

    /**
     * Sets the molecules.
     *
     * @param molecules the molecules
     */
    public void setMolecules(EnumMap<WaterStatus, Molecule> molecules) {
        this.molecules = molecules;
    }
    
    

}
