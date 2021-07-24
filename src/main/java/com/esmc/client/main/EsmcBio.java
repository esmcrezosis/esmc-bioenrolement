package com.esmc.client.main;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import com.esmc.client.swing.utils.MainFrame;
import com.esmc.client.utils.LibraryManager;
import com.esmc.client.utils.LicenseManager;

public class EsmcBio implements PropertyChangeListener {

	    // ===========================================================
	    // Private static final fields
	    // ===========================================================
	    private static final Set<String> LICENSES;

	    // ===========================================================
	    // Static constructor
	    // ===========================================================
	    static {
	        LICENSES = new HashSet<String>(1);
	        LICENSES.add("Biometrics.FingerExtraction");
	        LICENSES.add("Biometrics.FingerSegmentation");
	        LICENSES.add("Biometrics.Tools.NFIQ"); // Optional.
	       
	    }

	    // ===========================================================
	    // Public static methods
	    // ===========================================================
	    public static void main(String[] args) {
	        LibraryManager.initLibraryPath();
	        EsmcBio sample = new EsmcBio();
	        LicenseManager.getInstance().addPropertyChangeListener(sample);
	        try {
	            LicenseManager.getInstance().addLicenses();
	            LicenseManager.getInstance().obtain(LICENSES);
	        } catch (IOException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(null, e.toString());
	            return;
	        }

	        SwingUtilities.invokeLater(() -> {
	            try {
	                JFrame frame = new MainFrame();
	                Dimension d = new Dimension(1015, 625);
	                frame.setSize(d);
	                frame.setMinimumSize(new Dimension(800, 600));
	                frame.setPreferredSize(d);
	                frame.setResizable(true);
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                frame.setTitle("ESMC ENROLLEMENT BIOMETRIE");
	                frame.setLocationRelativeTo(null);
	                frame.setVisible(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	                JOptionPane.showMessageDialog(null, e.toString());
	            }
	        });
	    }

	    // ===========================================================
	    // Private fields
	    // ===========================================================
	    private final ProgressMonitor progressMonitor;

	    // ===========================================================
	    // Private methods
	    // ===========================================================
	    private EsmcBio() {
	        progressMonitor = new ProgressMonitor(null, "License obtain", "", 0, LICENSES.size());
	    }

	    // ===========================================================
	    // Event handling
	    // ===========================================================
	    @Override
	    public void propertyChange(PropertyChangeEvent evt) {
	        if (LicenseManager.PROGRESS_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
	            int progress = (Integer) evt.getNewValue();
	            progressMonitor.setProgress(progress);
	            String message = String.format("# of analyzed licenses: %d\n", progress);
	            progressMonitor.setNote(message);
	        }
	    }
	}


