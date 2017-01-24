package epstools;

import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.ghost4j.Ghostscript;
import org.ghost4j.GhostscriptException;

/**
 *
 * @author joel
 */
public class Epstools {

    /**
     * @param args the command line arguments
     * command: the master command resize|normalize|topdf
     * input: input file source
     * output: output file converted
     * colorProfile: color profile needed for conversion
     * size: output size in pixels, with format NxN
     * quality: output quality in dpi
     */
    public static void main(String[] args) 
    {
        BasicConfigurator.configure();
        // Instance of Ghostscript
        Ghostscript gs      = Ghostscript.getInstance();
        String command      = args[0];
        String input        = args[1];
        String output       = args[2];
        String colorProfile = args[3];
        String size         = args[4];
        String quality      = args[5];
        File inputFile      = new File(input);
        File outputFile     = new File(output);
        File iccFile        = new File(colorProfile);
        // Prepare command
        String[] gsArgs     = generateScript(command, inputFile, outputFile, iccFile, quality, size);
        try {
            gs.initialize(gsArgs);
            gs.exit();
        } catch (GhostscriptException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static String[] generateScript(String command, File inputFile, File outputFile, File icc, String quality, String size)
    {
        String driver = getDriver(command);
        String[] gsArgs = {
            "-dSAFER",
            "-dBATCH",
            "-dNOPAUSE",
            "-sDEVICE="+driver,
            "-r" + quality,
            "-g" + size,
            "-dOverrideICC=true",
            "-dEPSFitPage",
            "-sOutputICCProfile=" + icc,
            "-sOutputFile=" + outputFile,
            "-q",
            "-f"+inputFile
        };
        
        return gsArgs;
    }
    
    private static String getDriver(String command)
    {
        String driver = "pdfwrite";
        switch(command){
            case "resize":
                    driver   = "eps2write";
                    break;
            case "normalize":
                    driver   = "eps2write";
                    break;
            case "topdf":
                    driver   = "pdfwrite";
                    break;
            default:
                    break;
        }
        
        return driver;
    }
}