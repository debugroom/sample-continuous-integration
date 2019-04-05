package org.debugroom.sample.continuous.integration.domain.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.debugroom.sample.continuous.integration.apinfra.junit.category.UnitTest;
import org.debugroom.sample.continuous.integration.domain.model.entity.User;
import org.junit.experimental.categories.Category;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(Enclosed.class)
public class TestCoverageRuleTest {

    @Category(UnitTest.class)
    @RunWith(Theories.class)
    public static class TestCoverageRuleUnitTest{

        @DataPoints
        public static IntegerFixture[] integerFixture = {
                IntegerFixture.builder().integerArray(new Integer[]{0,1,2}).build(),
                IntegerFixture.builder().integerArray(new Integer[]{1,3,4}).build(),
                IntegerFixture.builder().integerArray(new Integer[]{5,2,3}).build(),
                IntegerFixture.builder().integerArray(new Integer[]{6,6,6}).build(),
        };

        @Theory
        public void testCoverage(IntegerFixture fixture){
            User user = User.builder().id(new Long(0)).build();
            TestCoverageRule testCoverageRule = new TestCoverageRule();
            testCoverageRule.testCoverage(user, fixture.integerArray);
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class IntegerFixture{
            Integer[] integerArray;
            String expected;
        }

    }

}
