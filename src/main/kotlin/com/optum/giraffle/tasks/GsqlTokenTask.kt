package com.optum.giraffle.tasks

import khttp.get
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import okhttp3.Request

open class GsqlTokenTask() : GsqlTokenAbstract() {

    @TaskAction
    fun initToken() {

        val urlBuilder = url().toHttpUrlOrNull().newBuilder()

        val httpUrl: HttpUrl.Builder = HttpUrl.parse(url()).newBuilder()
            .query("secret=${gsqlPluginExtension.authSecret.get()}")
        val r = Request.Builder()
            .url(url())

        val r = get(url = url(), params = mapOf(
            "secret" to gsqlPluginExtension.authSecret.get()
        ))
        with(logger) {
            // info("generated url: {}", r.url)
            info("status code: {}", r.statusCode)
            info("response: {}", r.jsonObject
        }

        when (r.jsonObject["error"]) {
            false -> gsqlPluginExtension.token.set(r.jsonObject["token"].toString())
            true -> throw GradleException(r.jsonObject["message"].toString())
        }
    }
}
