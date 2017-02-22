//
// Created by HuyLV-CT on 28-Dec-16.
//

#include <jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <android/log.h>

namespace patch
{
    template < typename T > std::string to_string( const T& n )
    {
        std::ostringstream stm ;
        stm << n ;
        return stm.str() ;
    }
}
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , "TAG jni",__VA_ARGS__ )
using namespace std;
using namespace cv;

char* itoa(int i, char b[]) {
    char const digit[] = "0123456789";
    char* p = b;
    if (i<0) {
        *p++ = '-';
        i *= -1;
    }
    int shifter = i;
    do { //Move to where representation ends
        ++p;
        shifter = shifter / 10;
    } while (shifter);
    *p = '\0';
    do { //Move back, inserting digits as u go
        *--p = digit[i % 10];
        i = i / 10;
    } while (i);
    return b;
}
void convertTo2(Mat m1, Mat &m2, double alpha, double beta) {
    int channels = 1;
    switch (m1.type()) {
        case CV_8UC3:
            for (int y = 0; y < m1.rows; y++) {
                for (int x = 0; x < m1.cols; x++) {
                    for (int c = 0; c < 3; c++) {
                        m2.at<Vec3b>(y, x)[c] = saturate_cast<uchar>(
                                alpha * (m1.at<Vec3b>(y, x)[c] - 128) + 128 + beta);
                    }
                }
            }
            break;
        case CV_8UC4:
            for (int y = 0; y < m1.rows; y++) {
                for (int x = 0; x < m1.cols; x++) {
                    for (int c = 0; c < 3; c++) {
                        m2.at<Vec4b>(y, x)[c] = saturate_cast<uchar>(
                                alpha * (m1.at<Vec4b>(y, x)[c] - 128) + 128 + beta);
                    }
                    m2.at<Vec4b>(y, x)[3] = saturate_cast<uchar>(m1.at<Vec4b>(y, x)[3]);
                }
            }
            break;
    }

}

void adjustVibrance(Mat srcMat, Mat &dstMat, int adjustValue) {
    adjustValue *= -1;
    switch (srcMat.type()) {
        case CV_8UC3:
            for (int y = 0; y < srcMat.rows; y++) {
                for (int x = 0; x < srcMat.cols; x++) {
                    Vec3b rgb = srcMat.at<Vec3b>(y, x);
                    int maxx = max(max(rgb[0], rgb[1]), rgb[2]);
                    int avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
                    float amt = (((float) abs(maxx - avg) * 2 / 255) * adjustValue) / 100;

                    for (int c = 0; c < 3; c++) {
                        if (rgb[c] != maxx) {
                            float temp = (maxx - rgb[c]) * amt;
                            dstMat.at<Vec3b>(y, x)[c] = saturate_cast<uchar>(
                                    srcMat.at<Vec3b>(y, x)[c] + temp);
                        } else {
                            dstMat.at<Vec3b>(y, x)[c] = saturate_cast<uchar>(
                                    srcMat.at<Vec3b>(y, x)[c]);
                        }
                    }
                }
            }
            break;
        case CV_8UC4:
            for (int y = 0; y < srcMat.rows; y++) {
                for (int x = 0; x < srcMat.cols; x++) {
                    Vec4b rgba = srcMat.at<Vec4b>(y, x);
                    int maxx = max(max(rgba[0], rgba[1]), rgba[2]);
                    int avg = (rgba[0] + rgba[1] + rgba[2]) / 3;
                    float amt = (((float) abs(maxx - avg) * 2 / 255) * adjustValue) / 100;

                    for (int c = 0; c < 3; c++) {
                        if (rgba[c] != maxx) {
                            float temp = (maxx - rgba[c]) * amt;
                            dstMat.at<Vec4b>(y, x)[c] = saturate_cast<uchar>(
                                    srcMat.at<Vec4b>(y, x)[c] + temp);
                        } else {
                            dstMat.at<Vec4b>(y, x)[c] = saturate_cast<uchar>(
                                    srcMat.at<Vec4b>(y, x)[c]);
                        }
                    }
                    dstMat.at<Vec4b>(y, x)[3] = saturate_cast<uchar>(srcMat.at<Vec4b>(y, x)[3]);
                }
            }
            break;
    }
}

void adjustTemperature(Mat srcMat, Mat &dstMat, int adjustValue) {
    switch (srcMat.type()) {
        case CV_8UC1:
            for (int y = 0; y < srcMat.rows; y++) {
                for (int x = 0; x < srcMat.cols; x++) {
                    dstMat.at<uchar>(y, x) = saturate_cast<uchar>(
                            srcMat.at<uchar>(y, x) + adjustValue);
                }
            }
            break;
        case CV_8UC3:
            for (int y = 0; y < srcMat.rows; y++) {
                for (int x = 0; x < srcMat.cols; x++) {
                    dstMat.at<Vec3b>(y, x)[0] = saturate_cast<uchar>(
                            srcMat.at<Vec3b>(y, x)[0] + adjustValue);
                    dstMat.at<Vec3b>(y, x)[1] = saturate_cast<uchar>(srcMat.at<Vec3b>(y, x)[1]);
                    dstMat.at<Vec3b>(y, x)[2] = saturate_cast<uchar>(
                            srcMat.at<Vec3b>(y, x)[2] - adjustValue);
                }
            }
            break;
        case CV_8UC4:
            for (int y = 0; y < srcMat.rows; y++) {
                for (int x = 0; x < srcMat.cols; x++) {
                    dstMat.at<Vec4b>(y, x)[0] = saturate_cast<uchar>(
                            srcMat.at<Vec4b>(y, x)[0] + adjustValue);
                    dstMat.at<Vec4b>(y, x)[1] = saturate_cast<uchar>(srcMat.at<Vec4b>(y, x)[1]);
                    dstMat.at<Vec4b>(y, x)[2] = saturate_cast<uchar>(
                            srcMat.at<Vec4b>(y, x)[2] - adjustValue);
                    dstMat.at<Vec4b>(y, x)[3] = saturate_cast<uchar>(srcMat.at<Vec4b>(y, x)[3]);
                }
            }
            break;
        default:
            LOGE("w %d h %d type %d", srcMat.rows, srcMat.cols, srcMat.type());
            break;
    }
}

void autofix(Mat srcMat, Mat &m, int clip) {
    int histSize = 256;
    float alpha, beta;
    double minGray = 0, maxGray = 0;

    //to calculate grayscale histogram
    Mat gray;
    if (srcMat.type() == CV_8UC1) gray = srcMat;
    else if (srcMat.type() == CV_8UC3) cvtColor(srcMat, gray, CV_BGR2GRAY);
    else if (srcMat.type() == CV_8UC4) cvtColor(srcMat, gray, CV_BGRA2GRAY);
    if (clip == 0) {
        // keep full available range
        minMaxLoc(gray, &minGray, &maxGray);
    }
    else {
        Mat hist; //the grayscale histogram

        float range[] = {0, 256};
        const float *histRange = {range};
        bool uniform = true;
        bool accumulate = false;
        calcHist(&gray, 1, 0, Mat(), hist, 1, &histSize, &histRange, uniform, accumulate);

        // calculate cumulative distribution from the histogram
        std::vector<float> accumulator(histSize);
        accumulator[0] = hist.at<float>(0);
        for (int i = 1; i < histSize; i++) {
            accumulator[i] = accumulator[i - 1] + hist.at<float>(i);
        }

        // locate points that cuts at required value
        float max = accumulator.back();
        clip *= (max / 100.0); //make percent as absolute
        clip /= 2.0; // left and right wings
        // locate left cut
        minGray = 0;
        while (accumulator[minGray] < clip)
            minGray++;

        // locate right cut
        maxGray = histSize - 1;
        while (accumulator[maxGray] >= (max - clip))
            maxGray--;
    }

    // current range
    float inputRange = maxGray - minGray;

    alpha = (histSize - 1) / inputRange;   // alpha expands current range to histsize range
    beta = -minGray * alpha;             // beta shifts current range so that minGray will go to 0

    // Apply brightness and contrast normalization
    // convertTo operates with saurate_cast
    srcMat.convertTo(m, -1, alpha, beta);

    // restore alpha channel from source
    if (m.type() == CV_8UC4) {
        int from_to[] = {3, 3};
        mixChannels(&srcMat, 1, &m, 1, from_to, 1);
    }
    return;
}

void adjustMat(Mat m1,Mat &m2,float exp,int temp,float cont,int bri,int vib){
    Mat m3 = m1.clone();
    m1.copyTo(m3);
    //LOGE("step2");
    if (exp != 1 || bri != 0) m1.convertTo(m2, -1, exp, bri);
    else m1.copyTo(m2);
    //LOGE("step3");
    if (temp != 0) adjustTemperature(m2, m3, temp);
    else m2.copyTo(m3);
    //LOGE("step4");
    if (cont != 1) convertTo2(m3, m2, exp, bri);
    else m3.copyTo(m2);
    //LOGE("step5");
    if (vib != 0) {
        adjustVibrance(m2, m3, vib);
        m3.copyTo(m2);
    }
}
Scalar bgr2ycrcb( cv::Scalar bgr )
{
    double R = bgr[ 2 ];
    double G = bgr[ 1 ];
    double B = bgr[ 0 ];
    double delta = 128; // Note: change this value if image type isn't CV_8U.

    double Y  = 0.299 * R + 0.587 * G + 0.114 * B;
    double Cr = ( R - Y ) * 0.713 + delta;
    double Cb = ( B - Y ) * 0.564 + delta;

    return Scalar( Y, Cr, Cb, 0 /* ignored */ );
}
Mat1b chromaKey(const Mat3b & imageBGR, Scalar chromaBGR, double tInner, double tOuter)
{
    // Basic outline:
    //
    // 1. Convert the image to YCrCb.
    // 2. Measure Euclidean distances of color in YCrBr to chroma value.
    // 3. Categorize pixels:
    //   * color distances below inner threshold count as foreground; mask value = 0
    //   * color distances above outer threshold count as background; mask value = 255
    //   * color distances between inner and outer threshold a linearly interpolated; mask value = [0, 255]

    assert(tInner <= tOuter);

    // Convert to YCrCb.
    assert(!imageBGR.empty());
    Size imageSize = imageBGR.size();
    Mat3b imageYCrCb;
    cvtColor(imageBGR, imageYCrCb, COLOR_BGR2YCrCb);
    Scalar chromaYCrCb = bgr2ycrcb(chromaBGR); // Convert a single BGR value to YCrCb.

    // Build the mask.
    Mat1b mask = Mat1b::zeros(imageSize);
    const Vec3d key(chromaYCrCb[0], chromaYCrCb[1], chromaYCrCb[2]);

    for (int y = 0; y < imageSize.height; ++y)
    {
        for (int x = 0; x < imageSize.width; ++x)
        {
            const Vec3d color(imageYCrCb(y, x)[0], imageYCrCb(y, x)[1], imageYCrCb(y, x)[2]);
            double distance = norm(key - color);

            if (distance < tInner)
            {
                // Current pixel is fully part of the background.
                mask(y, x) = 0;
            }
            else if (distance > tOuter)
            {
                // Current pixel is fully part of the foreground.
                mask(y, x) = 255;
            }
            else
            {
                // Current pixel is partially part both, fore- and background; interpolate linearly.
                // Compute the interpolation factor and clip its value to the range [0, 255].
                double d1 = distance - tInner;
                double d2 = tOuter - tInner;
                uint8_t alpha = static_cast<uint8_t>(255. * (d1 / d2));

                mask(y, x) = alpha;
            }
        }
    }

    return mask;
}
void clearBackground(Mat m1,Mat &m2,Mat1b mask){
    m1.copyTo(m2);
    LOGE("m1 type %d m2 type %d",m1.type(),m2.type());
//    cvtColor(m2,m2,CV_BGR2BGRA);
//    LOGE("m1 type %d m2 type %d",m1.type(),m2.type());

    for (int y = 0; y < m1.rows; y++)
    {
        for (int x = 0; x < m1.cols; x++)
        {
                uint8_t maskValue = mask(y, x);
                if (maskValue >= 255)
                {
//                    for (int k = 0; k < 3; k++) {
//                        m2.at<Vec4b>(y, x)[k] = saturate_cast<uchar>(m1.at<Vec3b>(y, x-startX)[k]);
//                    }
                    continue;
                }
                else if (maskValue <= 0)
                {
//                    for (int k = 0; k < 3; k++) {
//                        m2.at<Vec3b>(y, x)[k] = saturate_cast<uchar>(matBack.at<Vec3b>(y, x)[k]);
//                    }
                    m2.at<Vec4b>(y, x)[3] = saturate_cast<uchar>(0);
                }
                else
                {
//                    double alpha = 1. / static_cast<double>(maskValue);
//                    for (int k = 0; k < 3; k++) {
//                        m2.at<Vec4b>(y, x) = saturate_cast<uchar>(alpha * m1.at<Vec3b>(y-startY, x-startX)[k] + (1. - alpha) * matBack.at<Vec3b>(y, x)[k]);
//                    }
                    double alpha = ((float)maskValue*100)/255;
                    if(y==150&&x>50&&x<150) LOGE("alpha %f ",alpha);
                    m2.at<Vec4b>(y, x)[3] = saturate_cast<uchar>(alpha);
                }
        }
    }

//    mask.copyTo(m2);

//    for (int y = 0; y < m1.rows/2; y++) {
//        for (int x = 0; x < m1.cols/2; x++) {
//            m2.at<Vec4b>(y,x)[3] = saturate_cast<uchar>(0);
//        }
//    }
}
void clearBackground2(Mat m1,Mat &m2,Mat1b mask) {
    m1.copyTo(m2);
    LOGE("m1 type %d m2 type %d", m1.type(), m2.type());

    for (int y = 0; y < m1.rows; y++) {
        char s[1000] = "line ";
        char b[] = "bbb";
        strcat(s,itoa(y,b));
        for (int x = 0; x < m1.cols; x++) {
            int maskValue = mask(y, x);
//            if (maskValue >= 255) {
//
//            }
            strcat(s,itoa(maskValue,b));
        }
        LOGE("%s",s);
    }
}

extern "C" {
JNIEXPORT void JNICALL
Java_com_noah_photonext_MainActivity_native_1clearBackground2(JNIEnv *env, jobject thiz, jlong self,
                                                              jlong m_nativeObj) {
    Mat &srcMat = *((Mat *) self);
    Mat &dstMat = *((Mat *) m_nativeObj);

    if (srcMat.type() == CV_8UC3) cvtColor(srcMat, srcMat, CV_BGR2BGRA);

    srcMat.copyTo(dstMat);
    Scalar chroma(255, 255, 255, 255);
    double tInner = 21;
    double tOuter = 22;
    Mat1b mask = chromaKey(srcMat, chroma, tInner, tOuter);
    clearBackground2(srcMat, dstMat, mask);
}
JNIEXPORT void JNICALL
Java_com_noah_photonext_MainActivity_native_1clearBackground(JNIEnv *env, jobject thiz, jlong self,
                                                             jlong m_nativeObj) {
    Mat &srcMat = *((Mat *) self);
    Mat &dstMat = *((Mat *) m_nativeObj);

    if (srcMat.type() == CV_8UC3) cvtColor(srcMat, srcMat, CV_BGR2BGRA);

//    LOGE("mat type: %d",srcMat.type());

    srcMat.copyTo(dstMat);
    Scalar chroma(255, 255, 255, 255);
    double tInner = 21;
    double tOuter = 22;
    Mat1b mask = chromaKey(srcMat, chroma, tInner, tOuter);
    clearBackground(srcMat, dstMat, mask);
//    for(int i=0;i<10;i++){
//        Vec4b s = dstMat.at<Vec4b>(0,i);
//        int r = s[0];
//        int g = s[1];
//        int b = s[2];
//        int a = s[3];
//        LOGE("point %d %d %d %d",r,g,b,a);
//    }
}
JNIEXPORT void JNICALL
Java_com_noah_photonext_MainActivity_native_1adjustMat(JNIEnv *env, jobject thiz, jlong self,
                                                       jlong m_nativeObj, jfloat exp, jint temp,
                                                       jfloat cont, jint bri, jint vib) {
    Mat &srcMat = *((Mat *) self);
    Mat &dstMat = *((Mat *) m_nativeObj);

    CV_Assert(
            ((srcMat.type() == CV_8UC1) || (srcMat.type() == CV_8UC3) || (srcMat.type() == CV_8UC4))
            && ((dstMat.type() == CV_8UC1) || (dstMat.type() == CV_8UC3) ||
                (dstMat.type() == CV_8UC4)));
    CV_Assert(srcMat.type() == dstMat.type());

    adjustMat(srcMat, dstMat, exp, temp, cont, bri, vib);
}
JNIEXPORT void JNICALL
Java_com_noah_photonext_MainActivity_native_1adjustVibrance(JNIEnv *env, jobject thiz, jlong self,
                                                            jlong m_nativeObj, jint adjustValue) {
    Mat &srcMat = *((Mat *) self);
    Mat &dstMat = *((Mat *) m_nativeObj);

    CV_Assert(adjustValue >= -100 && adjustValue <= 100);
    CV_Assert(
            ((srcMat.type() == CV_8UC1) || (srcMat.type() == CV_8UC3) || (srcMat.type() == CV_8UC4))
            && ((dstMat.type() == CV_8UC1) || (dstMat.type() == CV_8UC3) ||
                (dstMat.type() == CV_8UC4)));
    CV_Assert(srcMat.type() == dstMat.type());

    adjustVibrance(srcMat, dstMat, adjustValue);
}
JNIEXPORT void JNICALL
Java_com_noah_photonext_MainActivity_native_1adjustContrast(JNIEnv *env, jobject thiz, jlong self,
                                                            jlong m_nativeObj, jfloat adjustValue,
                                                            jint beta) {
    Mat &srcMat = *((Mat *) self);
    Mat &dstMat = *((Mat *) m_nativeObj);

    CV_Assert(adjustValue >= -100 && adjustValue <= 100);
    CV_Assert(
            ((srcMat.type() == CV_8UC1) || (srcMat.type() == CV_8UC3) || (srcMat.type() == CV_8UC4))
            && ((dstMat.type() == CV_8UC1) || (dstMat.type() == CV_8UC3) ||
                (dstMat.type() == CV_8UC4)));
    CV_Assert(srcMat.type() == dstMat.type());

    convertTo2(srcMat, dstMat, adjustValue, beta);
}
JNIEXPORT void JNICALL
Java_com_noah_photonext_MainActivity_native_1adjustTemperature(JNIEnv *env, jobject thiz,
                                                               jlong self, jlong m_nativeObj,
                                                               jint adjustValue) {
    Mat &srcMat = *((Mat *) self);
    Mat &dstMat = *((Mat *) m_nativeObj);

    CV_Assert(adjustValue >= -100 && adjustValue <= 100);
    CV_Assert(
            ((srcMat.type() == CV_8UC1) || (srcMat.type() == CV_8UC3) || (srcMat.type() == CV_8UC4))
            && ((dstMat.type() == CV_8UC1) || (dstMat.type() == CV_8UC3) ||
                (dstMat.type() == CV_8UC4)));
    CV_Assert(srcMat.type() == dstMat.type());

//    LOGE("w %d h %d type %d %d",srcMat.rows,srcMat.cols ,srcMat.type() , dstMat.type());
    adjustTemperature(srcMat, dstMat, adjustValue);
}

JNIEXPORT void JNICALL
Java_com_noah_photonext_MainActivity_native_1adjustBrightness(JNIEnv *env, jobject thiz, jlong self,
                                                              jlong m_nativeObj, jfloat alpha,
                                                              jint beta) {
    Mat &srcMat = *((Mat *) self);
    Mat &dstMat = *((Mat *) m_nativeObj);

    CV_Assert(
            ((srcMat.type() == CV_8UC1) || (srcMat.type() == CV_8UC3) || (srcMat.type() == CV_8UC4))
            && ((dstMat.type() == CV_8UC1) || (dstMat.type() == CV_8UC3) ||
                (dstMat.type() == CV_8UC4)));
    CV_Assert(srcMat.type() == dstMat.type());

    //    LOGE("w %d h %d type %d %d",srcMat.rows,srcMat.cols ,srcMat.type() , dstMat.type());
    srcMat.convertTo(dstMat, -1, alpha, beta);
}

JNIEXPORT jstring JNICALL
Java_com_noah_photonext_MainActivity_getStringFrom(JNIEnv *env, jobject instance) {
    return env->NewStringUTF("ret urnValue");
}
JNIEXPORT void JNICALL
Java_com_noah_photonext_MainActivity_native_1autofix(JNIEnv *env, jobject thiz, jlong self,
                                                     jlong m_nativeObj, jfloat clip) {
    Mat &srcMat = *((Mat *) self);
    Mat &m = *((Mat *) m_nativeObj);

    CV_Assert(clip >= 0);
    CV_Assert(
            (srcMat.type() == CV_8UC1) || (srcMat.type() == CV_8UC3) || (srcMat.type() == CV_8UC4));

    autofix(srcMat, m, clip);
}
JNIEXPORT void JNICALL
Java_com_noah_photonext_MainActivity_native_1stack_1blur(JNIEnv *env, jobject thiz, jlong self,
                                                         jlong m_nativeObj, jint dotx, jint doty,
                                                         jint distance1, jint distance2) {
    Mat &srcMat = *((Mat *) self);
    Mat &m = *((Mat *) m_nativeObj);

    CV_Assert(distance1 >= 0 & dotx > 0 && doty > 0 & distance2 > 0);
    CV_Assert(
            (srcMat.type() == CV_8UC1) || (srcMat.type() == CV_8UC3) || (srcMat.type() == CV_8UC4));

    int h = srcMat.rows;
    int w = srcMat.cols;
    Mat p1 = srcMat(Rect(0, 0, w, doty - distance1 - distance2));
    Mat p1_blur = Mat();
    GaussianBlur(p1, p1_blur, Size(3, 3), 0);


}


}