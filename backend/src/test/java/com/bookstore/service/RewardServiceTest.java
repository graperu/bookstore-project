package com.bookstore.service;

import com.bookstore.entity.Coupon;
import com.bookstore.entity.PointTransaction;
import com.bookstore.entity.User;
import com.bookstore.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private PointTransactionRepository pointTransactionRepository;

    @Mock
    private RewardVoucherRepository rewardVoucherRepository;

    @Mock
    private UserRewardRepository userRewardRepository;

    @InjectMocks
    private RewardService rewardService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .yPoints(30000)
                .freeShipCoupons(0)
                .build();
    }

    @Test
    void exchangePoints_Success_Freeship() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        rewardService.exchangePoints("testuser", 20000, "FREESHIP");

        // Assert
        assertEquals(10000, testUser.getYPoints());
        assertEquals(2, testUser.getFreeShipCoupons());

        verify(userRepository, times(1)).save(testUser);
        verify(pointTransactionRepository, times(1)).save(any(PointTransaction.class));
        verify(couponRepository, never()).save(any(Coupon.class));
    }

    @Test
    void exchangePoints_Failure_NotEnoughPoints() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            rewardService.exchangePoints("testuser", 40000, "FREESHIP");
        });

        assertEquals("Không đủ Y-Points để đổi!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(pointTransactionRepository, never()).save(any(PointTransaction.class));
    }

    @Test
    void exchangePoints_Failure_InvalidDiscount20kPoints() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            rewardService.exchangePoints("testuser", 10000, "DISCOUNT_20K");
        });

        assertEquals("Cần đúng 20,000 điểm để đổi mã giảm 20K.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(pointTransactionRepository, never()).save(any(PointTransaction.class));
    }

    @Test
    void exchangePoints_Success_Discount20k() {
        // Arrange
        testUser.setYPoints(25000);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        rewardService.exchangePoints("testuser", 20000, "DISCOUNT_20K");

        // Assert
        assertEquals(5000, testUser.getYPoints());
        
        // Capture saved coupon and check values
        ArgumentCaptor<Coupon> couponCaptor = ArgumentCaptor.forClass(Coupon.class);
        verify(couponRepository, times(1)).save(couponCaptor.capture());
        
        Coupon savedCoupon = couponCaptor.getValue();
        assertNotNull(savedCoupon);
        assertTrue(savedCoupon.getCode().startsWith("VIP20K-"));
        assertEquals(20000.0, savedCoupon.getDiscountValue());
        assertEquals(1L, savedCoupon.getUserId());

        verify(userRepository, times(1)).save(testUser);
        verify(pointTransactionRepository, times(1)).save(any(PointTransaction.class));
    }
}
