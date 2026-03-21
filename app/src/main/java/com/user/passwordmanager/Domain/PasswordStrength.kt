package com.user.passwordmanager.Domain

import com.nulabinc.zxcvbn.Zxcvbn

object PasswordStrength {
    private val zxcvbn = Zxcvbn()

    /**
     * 评估密码强度
     * @param password 明文密码
     * @return 返回强度分数 0 到 4
     * 0: Too guessable (极弱，类似 "123456")
     * 1: Very guessable (弱)
     * 2: Somewhat guessable (中等，类似 "password123")
     * 3: Safely unguessable (强)
     * 4: Very unguessable (极强)
     */
    fun evaluateStrength(password: String): Int {
        if (password.isEmpty()) return 0

        // zxcvbn.measure() 会进行复杂的计算并返回一个 Measure 对象
        val measure = zxcvbn.measure(password)

        // 直接返回官方算好的 score (0, 1, 2, 3, 4)
        return measure.score
    }
}