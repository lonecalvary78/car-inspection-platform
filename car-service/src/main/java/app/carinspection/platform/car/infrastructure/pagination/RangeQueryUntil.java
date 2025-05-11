package app.carinspection.platform.car.infrastructure.pagination;

import io.helidon.config.Config;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RangeQueryUntil {
    private final Config paginationConfig = Config.create();
    private static final String MAXIMUM_RECORDS_PER_PAGE_KEY="pagination.maximum-records-per-page";

    public int getStartPosition(int currentPage) {
        return (currentPage <=0)?0:(currentPage-1)*getMaximumRecordsPerPage();
    }

    public int getEndPosition(int currentPage) {
        if(currentPage<=0) {
            throw new IllegalArgumentException("The current page should greater 0");
        } 
        return currentPage*getMaximumRecordsPerPage();
    }

    private int getMaximumRecordsPerPage() {
        return paginationConfig.get(MAXIMUM_RECORDS_PER_PAGE_KEY).asInt().orElse(20).intValue();
    }
}
