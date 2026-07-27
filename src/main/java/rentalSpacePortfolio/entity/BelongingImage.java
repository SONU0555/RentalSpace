package rentalSpacePortfolio.entity;


public interface BelongingImage<T> {
    ImageDetails getImageDetails();
    void setParent(T parentEntity);
}
