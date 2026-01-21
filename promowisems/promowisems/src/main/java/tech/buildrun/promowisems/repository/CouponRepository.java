package tech.buildrun.promowisems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.buildrun.promowisems.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {
}
