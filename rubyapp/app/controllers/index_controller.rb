require 'cassandra'

class IndexController < ApplicationController
  skip_before_filter :verify_authenticity_token
  $str=""
  $doc=""
  def index
  end
  
  def word
    $str = params[:search].to_s
    $amount = params[:amount]
    cluster = Cassandra.cluster
    session  = cluster.connect('dev')
    $query=session.execute("SELECT * FROM matrix WHERE doc = '#{$str}'").each do |row|
      p "doc = #{row['doc']}, simil = #{row['simil']}"
      $doc=row['simil']
    end
    $d1=(1-$doc[1].to_f)*100
    $d2=(1-$doc[3].to_f)*100
    $d3=(1-$doc[5].to_f)*100
    $d4=(1-$doc[7].to_f)*100
    $d5=(1-$doc[9].to_f)*100
    redirect_to index_pags_path
  end
  
  #def option
   # redirect_to index_index
  #end

end
