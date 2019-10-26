import { Component, OnInit } from '@angular/core';
import { NewsService } from '../news.service';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent implements OnInit {

  scrapped = [];
  page = 0;

  constructor(private newsService: NewsService) { }

  ngOnInit() {
    this.newsService.getPage(this.page).subscribe(s => {
      this.scrapped = s.data;
      this.page = s.page;
    });
  }

}
