import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from 'src/app/_services/article.service';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css']
})
export class ArticleComponent implements OnInit {

  link: string;
  article: any;

  constructor(private route: ActivatedRoute,
    private articleService: ArticleService) { }

  ngOnInit() {
    this.link = this.route.snapshot.paramMap.get('link');
    if (this.link) {
      this.articleService.getArticle(this.link).subscribe(r => this.article = r);
    }
  }

}
