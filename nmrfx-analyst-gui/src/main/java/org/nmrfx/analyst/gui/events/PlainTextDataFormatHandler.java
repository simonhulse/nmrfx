package org.nmrfx.analyst.gui.events;

import javafx.application.Platform;
import org.nmrfx.analyst.gui.molecule.MoleculeUtils;
import org.nmrfx.chemistry.io.MoleculeIOException;
import org.nmrfx.chemistry.io.SDFile;
import org.nmrfx.processor.datasets.Dataset;
import org.nmrfx.processor.gui.PolyChart;
import org.nmrfx.processor.gui.events.DataFormatEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle clipboard PLAIN_TEXT DataFormat
 */
public class PlainTextDataFormatHandler implements DataFormatEventHandler {
    private static final Logger log = LoggerFactory.getLogger(PlainTextDataFormatHandler.class);

    @Override
    public boolean handlePaste(Object o, PolyChart chart) {
        String content = (String) o;
        // Attempt to parse a molecule from the provided string
        if (pasteMolecule(content, chart)) {
            log.info("Parsed molecule from clipboard string.");
            return true;
        } else if (pasteDataset(content, chart)) {
            log.info("Parsed dataset from clipboard string.");
            return true;
        }
        return false;
    }

    /**
     * Attempts to paste a molecule parsed from molString to the canvas.
     * @param molString A string containing a molecule file contents.
     * @return True if molecule is parsed successfully, false otherwise.
     */
    private boolean pasteMolecule(String molString, PolyChart chart) {
        if (chart.getDataset() == null) {
            return false;
        }
        if (!SDFile.inMolFileFormat(molString)) {
            return false;
        }
        // Use the first line of the string as the molecule name if it is not blank, else prompt for a name
        String moleculeName = molString.split("\n")[0].trim();
        if (moleculeName.isEmpty()) {
            moleculeName = MoleculeUtils.moleculeNamePrompt();
        }
        try {
            SDFile.read(moleculeName, molString);
        } catch (MoleculeIOException e) {
            log.error("Unable to read molecule file. {}", e.getMessage());
            return false;
        }
        chart.setActiveChart();
        MoleculeUtils.addActiveMoleculeToCanvas();
        return true;
    }

    /**
     * Attempts to paste a dataset parsed from a datasetString to the canvas.
     * @param datasetString A string containing a dataset filename on the first line.
     * @return True if dataset is parsed successfully, false otherwise.
     */
    private boolean pasteDataset(String datasetString, PolyChart chart) {
        String[] items = datasetString.split("\n");
        if (items.length > 0) {
            Dataset dataset = Dataset.getDataset(items[0]);
            if (dataset != null) {
                Platform.runLater(() -> {
                    chart.setActiveChart();
                    for (String item : items) {
                        Dataset dataset1 = Dataset.getDataset(item);
                        if (dataset1 != null) {
                            chart.getController().addDataset(dataset1, true, false);
                        }
                    }

                });
            return true;
            }
        }
        return false;
    }
}