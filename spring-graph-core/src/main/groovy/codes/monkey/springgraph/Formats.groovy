package codes.monkey.springgraph

/**
 * Created by jzietsman on 3/18/16.
 */
class Formats {

    static Format<byte[]> IMAGE(format) {
        new ImageFormat(imageFormat: format)
    }

    static Format<byte[]> PNG = IMAGE('png')

    static Format<byte[]> SVG = IMAGE('svg')

    static Format<String> DOT = new DotFormat()

    static Format<Map> VISJS = new VisFormat()
}
