#!/usr/bin/env -S scala-cli shebang -q

//> using scala "3.7.1"
//> using dep "com.github.scopt::scopt::4.1.0"

import scopt.{OParser, Read}
import java.time.format.DateTimeFormatter
import java.time._
import java.util.concurrent.TimeUnit
import scala.util.Try

case class Args(
                  since: LocalDateTime,
                  until: Option[LocalDateTime],
                  zoneId: ZoneId
               )


// gegebenenfalls scopt-Read-Instanzen bereitstellen
given Read[LocalDateTime] with
   def arity: Int = 1

   def reads:  String => LocalDateTime =
      s => Try(LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
         .orElse(Try(LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE))
            .map(_.atStartOfDay))
         .getOrElse(
            throw new IllegalArgumentException(
               "Not a valid timestamp, correct format is `yyyy-mm-dd` OR `yyyy-mm-ddTHH:MM:SS`."
            ))

given Read[ZoneId] with
   def arity: Int = 1
   def reads:  String => ZoneId =
      s => Try(ZoneId.of(s))
         .getOrElse(throw new IllegalArgumentException(s"'$s' is not a valid timezone id"))

val builder = OParser.builder[Args]

import builder.*

val parser = OParser.sequence(
   programName("time-since.sc"),
   head("time-since", "1.x"),

   arg[LocalDateTime]("<timestamp>")
      .text("Format: `yyyy-mm-dd` or `yyyy-mm-ddTHH:MM:SS`.")
      .action((ts, cfg) => cfg.copy(since = ts)),

   opt[LocalDateTime]('u', "until")
      .text("Format: `yyyy-mm-dd` or `yyyy-mm-ddTHH:MM:SS`. Defaults to NOW.")
      .action((ts, cfg) => cfg.copy(until = Some(ts))),

   opt[ZoneId]('z', "zone-id")
      .text("Example: Europe/Bucharest")
      .action((z, cfg) => cfg.copy(zoneId = z)),

   help("help").text("Show this help message")
)

val defaults = Args(
   since = LocalDateTime.MIN,
   until = None,
   zoneId = ZoneId.systemDefault()
)

val cfg = OParser.parse(parser, args, defaults) match
   case Some(c) => c
   case None =>
      sys.exit(1)
      defaults

val sinceZ = cfg.since.atZone(cfg.zoneId)
val untilZ = cfg.until.map(_.atZone(cfg.zoneId))
   .getOrElse(ZonedDateTime.now(cfg.zoneId))
val sinceMs = sinceZ.toInstant.toEpochMilli
val untilMs = untilZ.toInstant.toEpochMilli

println()
println(s"Since:   ${sinceZ.format(DateTimeFormatter.RFC_1123_DATE_TIME)}")
println(s"Until:   ${untilZ.format(DateTimeFormatter.RFC_1123_DATE_TIME)}")

val totalMs = untilMs - sinceMs
val days = TimeUnit.MILLISECONDS.toDays(totalMs)
val remMs1 = totalMs - TimeUnit.DAYS.toMillis(days)
val hours = TimeUnit.MILLISECONDS.toHours(remMs1)
val remMs2 = remMs1 - TimeUnit.HOURS.toMillis(hours)
val minutes = TimeUnit.MILLISECONDS.toMinutes(remMs2)
val remMs3 = remMs2 - TimeUnit.MINUTES.toMillis(minutes)
val seconds = TimeUnit.MILLISECONDS.toSeconds(remMs3)

println()
println(s"Elapsed: $days days, $hours hours, $minutes minutes, $seconds seconds")
println()

println(f"Years:   ${totalMs / (1000.0 * 60 * 60 * 24 * 365.24)}%11.2f")
println(f"Months:  ${totalMs / (1000.0 * 60 * 60 * 24 * 30.417)}%11.2f")
println(f"Weeks:   ${totalMs / (1000.0 * 60 * 60 * 24 * 7)}%11.2f")
println(f"Days:    ${totalMs / (1000.0 * 60 * 60 * 24)}%11.2f")
println(f"Hours:   ${totalMs / (1000.0 * 60 * 60)}%11.2f")
println(f"Minutes: ${totalMs / (1000.0 * 60)}%11.2f")
println()
