package jobs;

import play.jobs.Job;

/**
 * 异步下载指定url地址序列的的图片
 * @author royguo1988@gmail.com
 */
   public class ImageDownloader extends Job {
     private String[] urls;
     private String dir;

     public ImageDownloader(){}
     public ImageDownloader(String[] urls,String dir){
       this.urls = urls;
       this.dir = dir;
     }

     @Override
     public void doJob() throws Exception {
       if(urls!=null && urls.length > 0){
         for (int i = 0; i < urls.length; i++) {
           String url = urls[i];

         }
       }
     }
   }