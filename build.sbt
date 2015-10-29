/*
 * Exclude these files:
 *  - Test object
 *  - Non OOP implementation (AerialImage)
 *
 * Code taken from: http://sandarenu.blogspot.fr/2012/04/exclude-resource-files-from-jar-file.html
 */
val excludeFileRegx = """(Test|AerialImage).*?\.class""".r

// Common settings
lazy val commonSettings = Seq(
  organization := "com.tncy.top",
  organizationName := "Telecom Nancy - TOP",
  version := "1.0.0",
  //scalaVersion := "2.9.2"
  crossScalaVersions := Seq("2.9.2", "2.10.6", "2.11.7")
)

// Root project
lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "imagewrapper",
    // Use mappings task to exclude files
    mappings in (Compile, packageBin) ~= { (ms: Seq[(File, String)]) =>
      ms filter {
        case (file, toPath) => {
          val shouldExclude = excludeFileRegx.pattern.matcher(file.getName).matches
          //println("===========" + file + "  " + shouldExclude)
          !shouldExclude
        }
      }
    }
  )
