package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class    ReviewRepositoryTest {
    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewRepositoryTest(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Test
    public void ReviewRepository_SaveAll_ReturnsSavedReview() {
        Review review = Review.builder()
                .title("title")
                .content("content")
                .stars(5)
                .build();

        Review savedReview = reviewRepository.save(review);

        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void ReviewRepostory_GetAll_ReturnsMoreThenOneReview() {
        Review review = Review.builder()
                .title("title")
                .content("content")
                .stars(5)
                .build();
        Review review2 = Review.builder()
                .title("title")
                .content("content")
                .stars(5)
                .build();

        reviewRepository.save(review);
        reviewRepository.save(review2);

        List<Review> reviewList = reviewRepository.findAll();

        assertThat(reviewList).isNotNull();
        assertThat(reviewList.size()).isEqualTo(2);
    }

    @Test
    public void ReviewRepository_FindById_ReturnsSavedReview() {
        Review review = Review.builder()
                .title("title")
                .content("content")
                .stars(5)
                .build();

        reviewRepository.save(review);

        Review reviewReturn = reviewRepository
                .findById(review.getId())
                .get();

        assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewRepository_UpdateReview_ReturnReview() {
        Review review = Review.builder().title("title").content("content").stars(5).build();

        reviewRepository.save(review);

        Review reviewSave = reviewRepository.findById(review.getId()).get();
        reviewSave.setTitle("new title");
        reviewSave.setContent("new content");

        assertThat(reviewSave.getTitle()).isEqualTo("new title");
        assertThat(reviewSave.getContent()).isEqualTo("new content");
        assertThat(reviewSave.getStars()).isEqualTo(5);
    }

    @Test
    public void ReviewRepository_ReviewDelete_ReturnReviewIsEmpty() {
        Review review = Review.builder()
                .title("title")
                .content("content")
                .stars(5)
                .build();

        reviewRepository.save(review);

        reviewRepository.deleteById(review.getId());
        Optional<Review> reviewReturn = reviewRepository.findById(review.getId());

        assertThat(reviewReturn).isEmpty();
    }
}