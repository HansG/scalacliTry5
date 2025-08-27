#!/usr/bin/env -S scala-cli shebang -q

//> using scala "3.7.1"
//> using toolkit default
//> using dep "com.monovore::decline:2.5.0"

import cats.syntax.all.given
import com.monovore.decline.*
import sttp.client4.quick.*
import upickle.default.*
import java.net.InetAddress

val domainsToCleanUp = List(
  "corporate.net"
)

val toUpdate = List(
  "confluence.corporate.net",
  "kibana.corporate.net",
  "elk.corporate.net",
  "elk.uat.corporate.net",
  //...
)

def run(apiKey: String, profileId: String, dryRun: Boolean) =
  case class GetResponse(data: List[Map[String, String]])
    derives ReadWriter

  lazy val getProfile =
    val resp = quickRequest
      .get(uri"https://api.nextdns.io/profiles/$profileId/rewrites")
      .header("X-Api-Key", apiKey)
      .send()
    upickle.default.read[GetResponse](resp.body.toString)

  def post(name: String, content: String) =
    println(s"Adding $name (ip: $content)")
    if (!dryRun) then quickRequest
      .post(uri"https://api.nextdns.io/profiles/$profileId/rewrites")
      .header("X-Api-Key", apiKey)
      .header("Content-Type", "application/json")
      .body(upickle.default.write(
        Map(
          "name" -> name,
          "content" -> content
        )
      ))
      .send()

  def patch(id: String, name: String, content: String) =
    println(s"Updating $name (ip: $content)")
    if dryRun then quickRequest
      .patch(uri"https://api.nextdns.io/profiles/$profileId/rewrites/$id")
      .header("X-Api-Key", apiKey)
      .header("Content-Type", "application/json")
      .body(upickle.default.write(
        Map(
          "content" -> content
        )
      ))
      .send()

  def delete(id: String, name: String) =
    println(s"Deleting $name")
    if !dryRun then quickRequest
      .delete(uri"https://api.nextdns.io/profiles/$profileId/rewrites/$id")
      .header("X-Api-Key", apiKey)
      .send()

  def skip(name: String, content: String) =
    println(s"Skipping $name (ip: $content)")

  for name <- toUpdate do
    val address = InetAddress.getByName(name)
    val ip = address.getHostAddress.nn

    getProfile.data.find: entry =>
      entry.get("name").contains(name)
    match
      case None =>
        post(name, ip)
      case Some(entry) if !entry.get("content").contains(ip) =>
        patch(entry("id"), name, ip)
      case Some(_) =>
        skip(name, ip)

  for domain <- domainsToCleanUp do
    val toDelete = getProfile.data.filter: entry =>
      entry.getOrElse("name", "").endsWith(domain) &&
        !toUpdate.contains(entry("name"))
    for entry <- toDelete do
      delete(entry("id"), entry("name"))

object Main extends CommandApp(
  name = "nextdns-vpn-update",
  header = "Update NextDNS's rewrites based on the current DNS (corporate VPN)",
  main =
    val apiKey = Opts
      .option[String]("api-key", help = "NextDNS API key")
      .orElse(Opts.env[String]("NEXTDNS_API_KEY", help = "NextDNS API key"))
    val profileId = Opts
      .option[String]("profile-id", help = "NextDNS profile ID")
      .orElse(Opts.env[String]("NEXTDNS_PROFILE_ID", help = "NextDNS profile ID"))
    val dryRun = Opts
      .flag("dry-run", help = "Dry run").orFalse

    (apiKey, profileId, dryRun).mapN(run)
)