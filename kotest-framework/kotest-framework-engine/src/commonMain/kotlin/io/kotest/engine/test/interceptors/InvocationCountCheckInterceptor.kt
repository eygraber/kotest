package io.kotest.engine.test.interceptors

import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestScope
import io.kotest.core.test.TestType
import io.kotest.mpp.log
import kotlin.time.Duration

/**
 * Checks that the user has not tried to use an invalid invocation count.
 */
internal object InvocationCountCheckInterceptor : TestExecutionInterceptor {

   override suspend fun intercept(
      testCase: TestCase,
      scope: TestScope,
      test: suspend (TestCase, TestScope) -> TestResult
   ): TestResult {
      log { "InvocationCountCheckInterceptor: Checking that invocation count is 1 for containers" }
      return if (testCase.config.invocations > 1 && testCase.type == TestType.Container)
         TestResult.Error(Duration.ZERO, Exception("Cannot execute multiple invocations in parent tests"))
      else
         test(testCase, scope)
   }
}
