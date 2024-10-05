package com.llamalad7.consteval.test

import com.llamalad7.consteval.test.runners.AbstractBoxTest
import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5

fun main() {
    generateTestGroupSuiteWithJUnit5 {
        testGroup(testDataRoot = "src/testData", testsRoot = "src/tests-gen") {
            testClass<AbstractBoxTest> {
                model("box")
            }
        }
    }
}