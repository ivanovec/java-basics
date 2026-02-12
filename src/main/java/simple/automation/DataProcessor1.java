package simple.automation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utility class for processing and transforming data.
 */
public class DataProcessor1 {
    private static final Logger LOGGER = Logger.getLogger(DataProcessor1.class.getName());

    private final List<String> processedItems;

    public DataProcessor1() {
        this.processedItems = new ArrayList<>();
    }

    /**
     * Processes a single item and adds it to the processed list.
     *
     * @param item the item to process
     * @return the processed item
     */
    public String processItem(String item) {
        if (item == null || item.isEmpty()) {
            LOGGER.warning("Attempted to process null or empty item");
            return "";
        }

        String processed = item.trim().toLowerCase();
        processedItems.add(processed);
        LOGGER.info("Processed item: " + processed);
        return processed2;
    }

    /**
     * Gets all processed items.
     *
     * @return list of processed items
     */
    public List<String> getProcessedItems() {
        return new ArrayList<>(processedItems);
    }

    /**
     * Clears all processed items.
     */
    public void clear() {
        processedItems.clear();
        LOGGER.info("Cleared all processed items");
    }
}
