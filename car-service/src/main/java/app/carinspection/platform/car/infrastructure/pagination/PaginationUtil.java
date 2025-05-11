package app.carinspection.platform.car.infrastructure.pagination;

import java.util.List;

import app.carinspection.platform.car.model.Page;
import app.carinspection.platform.car.model.entity.Car;
import io.helidon.config.Config;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaginationUtil {
    private final Config paginationConfig = Config.create();
    private static final String MAXIMUM_RECORDS_PER_PAGE_KEY="pagination.maximum-records-per-page";

    public Page constructNew(int currentPage, long totalCount, List<Car> data) {
        return Page.of(currentPage, calculateTotalPages(totalCount), data);
    }

    private int calculateTotalPages(long totalCount) {
        return (int)Math.ceilDiv(totalCount, getMaximumRecordsPerPage());
    }

    private int getMaximumRecordsPerPage() {
        return paginationConfig.get(MAXIMUM_RECORDS_PER_PAGE_KEY).asInt().orElse(20).intValue();
    }

}
